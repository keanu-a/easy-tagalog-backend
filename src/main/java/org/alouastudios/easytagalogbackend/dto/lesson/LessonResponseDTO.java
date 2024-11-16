package org.alouastudios.easytagalogbackend.dto.lesson;

import java.util.Set;
import java.util.UUID;

public record LessonResponseDTO(
        UUID uuid,
        String title,
        Set<QuestionResponseDTO> questions
) {
}
