package ru.urfu.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.tasktracker.entity.Task;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatusOrderById(String status);

    @Query(value = "select * from tasks where project_id = :projectId order by id", nativeQuery = true)
    List<Task> findByProjectIdSql(@Param("projectId") Long projectId);
}
