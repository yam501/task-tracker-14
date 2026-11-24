package ru.urfu.tasktracker.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.tasktracker.entity.*;
import ru.urfu.tasktracker.repository.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrackerService {
    private final EmployeeRepository employees;
    private final ProjectRepository projects;
    private final RepositoryInfoRepository repositories;
    private final TaskRepository tasks;
    private final ParticipationRepository participations;
    private final EntityManager entityManager;

    public TrackerService(EmployeeRepository employees, ProjectRepository projects,
                          RepositoryInfoRepository repositories, TaskRepository tasks,
                          ParticipationRepository participations, EntityManager entityManager) {
        this.employees = employees;
        this.projects = projects;
        this.repositories = repositories;
        this.tasks = tasks;
        this.participations = participations;
        this.entityManager = entityManager;
    }

    @Transactional
    public void createDemoData() {
        if (projects.count() > 0) {
            return;
        }

        Employee misha = employees.save(new Employee("Михаил"));
        Employee anna = employees.save(new Employee("Анна"));
        RepositoryInfo siteRepository = repositories.save(new RepositoryInfo("https://example.com/site.git"));
        RepositoryInfo botRepository = repositories.save(new RepositoryInfo("https://example.com/bot.git"));
        Project site = projects.save(new Project("Сайт", siteRepository));
        Project bot = projects.save(new Project("Бот", botRepository));

        tasks.save(site.addTask("Создать главную страницу", "TODO"));
        tasks.save(site.addTask("Добавить форму входа", "IN_PROGRESS"));
        tasks.save(site.addTask("Настроить сборку", "DONE"));
        tasks.save(bot.addTask("Добавить команду помощи", "TODO"));
        tasks.save(bot.addTask("Настроить запуск бота", "DONE"));

        participations.save(new Participation(misha, site, "Разработчик", LocalDate.of(2026, 11, 1)));
        participations.save(new Participation(misha, bot, "Разработчик", LocalDate.of(2026, 11, 2)));
        participations.save(new Participation(anna, site, "Тестировщик", LocalDate.of(2026, 11, 3)));
    }

    @Transactional(readOnly = true)
    public List<String> describeProjects() {
        return projects.findAll(Sort.by("id")).stream()
                .map(project -> project.getName() + ": задач " + project.getTasks().size()
                        + ", репозиторий " + project.getRepository().getUrl()
                        + " (вымышленный адрес для примера)")
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> describeParticipations() {
        return participations.findAll(Sort.by("id")).stream()
                .map(participation -> participation.getEmployee().getName() + " / "
                        + participation.getProject().getName() + " / " + participation.getRole()
                        + " / " + participation.getJoinedAt())
                .toList();
    }

    @Transactional(readOnly = true)
    public Long firstProjectId() {
        return projects.findAll(Sort.by("id")).get(0).getId();
    }

    @Transactional(readOnly = true)
    public List<String> findTasksByStatus(String status) {
        return tasks.findByStatusOrderById(status).stream().map(this::describeTask).toList();
    }

    @Transactional(readOnly = true)
    public List<String> findTasksByProjectSql(Long projectId) {
        return tasks.findByProjectIdSql(projectId).stream().map(this::describeTask).toList();
    }

    @Transactional(readOnly = true)
    public List<String> findProjectsByEmployeeJpql(String name) {
        return projects.findProjectsByEmployeeName(name).stream().map(Project::getName).toList();
    }

    @Transactional(readOnly = true)
    public List<String> filterTasks(String title, String status, Long projectId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);
        List<Predicate> conditions = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            conditions.add(builder.like(task.get("title"), "%" + title.trim() + "%"));
        }
        if (status != null && !status.isBlank()) {
            conditions.add(builder.equal(task.get("status"), status.trim()));
        }
        if (projectId != null) {
            conditions.add(builder.equal(task.get("project").get("id"), projectId));
        }

        query.select(task);
        if (!conditions.isEmpty()) {
            query.where(conditions.toArray(new Predicate[0]));
        }
        query.orderBy(builder.asc(task.get("id")));
        return entityManager.createQuery(query).getResultList().stream().map(this::describeTask).toList();
    }

    private String describeTask(Task task) {
        return task.getTitle() + " / " + task.getStatus() + " / " + task.getProject().getName();
    }
}
