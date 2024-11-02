package org.alouastudios.easytagalogbackend.dto.response.lessonResponse;

import java.util.UUID;

public record LessonWordResponseDTO(
        UUID uuid,
        String tagalog
) {
}
