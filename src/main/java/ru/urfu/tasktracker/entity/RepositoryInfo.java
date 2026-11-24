package ru.urfu.tasktracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "repositories")
public class RepositoryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    protected RepositoryInfo() {
    }

    public RepositoryInfo(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
