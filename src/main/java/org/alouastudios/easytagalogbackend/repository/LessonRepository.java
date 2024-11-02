package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Optional<Lesson> findByUuid(UUID uuid);
}
