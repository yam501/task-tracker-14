package ru.urfu.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.tasktracker.entity.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}
