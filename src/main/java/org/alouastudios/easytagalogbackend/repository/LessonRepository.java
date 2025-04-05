package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
