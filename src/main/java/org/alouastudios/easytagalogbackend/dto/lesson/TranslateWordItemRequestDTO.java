package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TranslateWordItemRequestDTO extends LessonItemRequestDTO {
    private UUID englishUuid;
    private List<UUID> wordOptionsUuid;
    private UUID wordAnswerUuid;
}
