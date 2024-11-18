package org.alouastudios.easytagalogbackend.dto.lesson;

import java.util.UUID;

public record LessonOptionResponseDTO(
        UUID uuid,
        String tagalog
) {
}
