package ru.urfu.tasktracker.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repository_id", nullable = false, unique = true)
    private RepositoryInfo repository;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks = new ArrayList<>();

    protected Project() {
    }

    public Project(String name, RepositoryInfo repository) {
        this.name = name;
        this.repository = repository;
    }

    public Task addTask(String title, String status) {
        Task task = new Task(title, status, this);
        tasks.add(task);
        return task;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RepositoryInfo getRepository() {
        return repository;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
