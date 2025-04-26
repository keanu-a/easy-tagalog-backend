package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.lessons.LessonItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LessonItemRepository extends JpaRepository<LessonItem, Long> {
    Optional<LessonItem> findLessonItemByUuid(UUID uuid);
}
