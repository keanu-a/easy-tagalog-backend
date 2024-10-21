package org.alouastudios.easytagalogbackend.dto;

import java.util.List;

public record LessonDTO (
    String title,
    List<QuestionDTO> questions
) {}
