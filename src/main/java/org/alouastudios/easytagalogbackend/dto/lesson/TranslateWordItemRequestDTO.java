package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TranslateWordItemRequestDTO extends LessonItemRequestDTO {
    private List<UUID> options;
    private UUID answer;
}
