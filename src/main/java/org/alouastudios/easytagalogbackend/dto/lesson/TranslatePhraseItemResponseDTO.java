package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TranslatePhraseItemResponseDTO extends LessonItemResponseDTO {
    private String english;
    private List<PhraseResponseDTO> options;
    private UUID answer;

    public TranslatePhraseItemResponseDTO(UUID uuid, String english, List<PhraseResponseDTO> options, UUID answer) {
        super(uuid);
        this.english = english;
        this.options = options;
        this.answer = answer;
    }
}
