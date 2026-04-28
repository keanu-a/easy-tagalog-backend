package org.alouastudios.easytagalogbackend.dto.lesson;

import java.util.UUID;

public record LessonSummaryDTO(
        UUID uuid,
        String title
) {
}
