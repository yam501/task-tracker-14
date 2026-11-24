package ru.urfu.tasktracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.urfu.tasktracker.service.TrackerService;
import java.util.List;

@Component
public class DemoRunner implements CommandLineRunner {
    private final TrackerService service;

    public DemoRunner(TrackerService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        service.createDemoData();
        Long projectId = service.firstProjectId();

        print("1. Связи 1:N и 1:1", service.describeProjects());
        print("2. Связь N:M через участие", service.describeParticipations());
        print("3. Запрос из имени метода: статус TODO", service.findTasksByStatus("TODO"));
        print("4. Native SQL: задачи первого проекта", service.findTasksByProjectSql(projectId));
        print("5. JPQL: проекты Михаила", service.findProjectsByEmployeeJpql("Михаил"));
        print("6. Criteria: без фильтров", service.filterTasks(null, null, null));
        print("7. Criteria: статус TODO", service.filterTasks(null, "TODO", null));
        print("8. Criteria: TODO в первом проекте", service.filterTasks(null, "TODO", projectId));
        print("9. Criteria: название, статус и проект",
                service.filterTasks("главную", "TODO", projectId));
        print("10. Criteria: пустые строки не задают условия", service.filterTasks("  ", "", null));
        print("11. Criteria: совпадений нет", service.filterTasks("Несуществующая задача", null, null));
        System.out.println("\nДемонстрация завершена.");
    }

    private void print(String title, List<String> rows) {
        System.out.println("\n" + title + " (найдено: " + rows.size() + ")");
        rows.forEach(System.out::println);
    }
}
