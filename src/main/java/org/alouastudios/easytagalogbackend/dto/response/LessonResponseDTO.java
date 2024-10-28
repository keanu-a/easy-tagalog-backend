package org.alouastudios.easytagalogbackend.dto.response;

import java.util.List;

public record LessonResponseDTO(
        String title,
        List<QuestionResponseDTO> questions
) {
}
