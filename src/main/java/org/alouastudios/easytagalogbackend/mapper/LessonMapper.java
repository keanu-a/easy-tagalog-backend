package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.response.LessonResponseDTO;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.model.lessons.Question;

import java.util.List;

public class LessonMapper {

    // TODO 10/28: Implement mapping questions, find a good solution
    // TODO: Thinking maybe create a question mapper function to create QuestionResponseDTO's

    public LessonResponseDTO toResponseDTO(Lesson lesson, List<Question> questions) {

        return new LessonResponseDTO(
                lesson.getTitle(),
                questions
        );
    }

    public void toEntity(LessonResponseDTO lessonResponseDTO, Lesson lesson) {}
}
