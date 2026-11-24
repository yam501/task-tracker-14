package ru.urfu.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.tasktracker.entity.Project;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select p.project from Participation p where p.employee.name = :name order by p.project.id")
    List<Project> findProjectsByEmployeeName(@Param("name") String name);
}
