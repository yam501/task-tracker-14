package ru.urfu.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.tasktracker.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
