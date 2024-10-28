package org.alouastudios.easytagalogbackend.dto;

import java.util.List;

public record LessonRequestDTO(
    String title,
    List<QuestionRequestDTO> questions
) { }
