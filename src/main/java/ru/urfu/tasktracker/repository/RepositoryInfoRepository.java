package ru.urfu.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.tasktracker.entity.RepositoryInfo;

public interface RepositoryInfoRepository extends JpaRepository<RepositoryInfo, Long> {
}
