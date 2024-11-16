package org.alouastudios.easytagalogbackend.dto.lesson;

import java.util.UUID;

public record LessonEnglishResponseDTO(
        UUID uuid,
        String tagalog
) {
}
