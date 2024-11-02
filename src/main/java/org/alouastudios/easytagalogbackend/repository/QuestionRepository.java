package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.lessons.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
