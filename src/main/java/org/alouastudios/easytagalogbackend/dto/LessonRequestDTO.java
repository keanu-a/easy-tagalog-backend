package org.alouastudios.easytagalogbackend.dto;

import java.util.Set;

public record LessonRequestDTO(
    String title,
    Set<QuestionRequestDTO> questions
) { }
