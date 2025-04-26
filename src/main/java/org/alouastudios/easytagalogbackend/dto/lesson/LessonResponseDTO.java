package org.alouastudios.easytagalogbackend.dto.lesson;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record LessonResponseDTO(
        UUID uuid,
        String title,
        List<LessonItemResponseDTO> items
) {
}
