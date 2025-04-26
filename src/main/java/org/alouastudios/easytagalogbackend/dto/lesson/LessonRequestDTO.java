package org.alouastudios.easytagalogbackend.dto.lesson;

import java.util.List;

public record LessonRequestDTO(
    String title,
    List<LessonItemRequestDTO> items
) {
}
