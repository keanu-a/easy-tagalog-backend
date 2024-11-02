package org.alouastudios.easytagalogbackend.dto.response.lessonResponse;

import java.util.UUID;

public record LessonPhraseResponseDTO(
        UUID uuid,
        String tagalog
) {
}
