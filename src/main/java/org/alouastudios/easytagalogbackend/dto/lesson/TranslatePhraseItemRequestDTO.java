package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TranslatePhraseItemRequestDTO extends LessonItemRequestDTO {
    private UUID phraseUuid;
    private List<UUID> phraseOptionsUuid;
    private UUID phraseAnswerUuid;
}
