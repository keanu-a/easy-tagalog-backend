package org.alouastudios.easytagalogbackend.dto.lesson;

import java.util.UUID;

public record LessonTagalogResponseDTO(
        UUID uuid,
        String tagalog
) {
}
