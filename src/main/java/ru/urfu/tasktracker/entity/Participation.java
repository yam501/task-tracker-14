package ru.urfu.tasktracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "participations", uniqueConstraints =
        @UniqueConstraint(columnNames = {"employee_id", "project_id"}))
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String role;

    @Column(name = "joined_at", nullable = false)
    private LocalDate joinedAt;

    protected Participation() {
    }

    public Participation(Employee employee, Project project, String role, LocalDate joinedAt) {
        this.employee = employee;
        this.project = project;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Project getProject() {
        return project;
    }

    public String getRole() {
        return role;
    }

    public LocalDate getJoinedAt() {
        return joinedAt;
    }
}
