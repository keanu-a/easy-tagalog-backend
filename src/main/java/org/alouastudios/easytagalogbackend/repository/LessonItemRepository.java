package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.lessons.LessonItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonItemRepository extends JpaRepository<LessonItem, Long> {
    LessonItem findLessonItemByUuid(String uuid);
}
