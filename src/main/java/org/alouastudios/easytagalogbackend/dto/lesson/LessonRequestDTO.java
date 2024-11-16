package org.alouastudios.easytagalogbackend.dto.lesson;

import java.util.Set;

public record LessonRequestDTO(
    String title,
    Set<QuestionRequestDTO> questions
) { }
