package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TranslatePhraseItemResponseDTO extends LessonItemResponseDTO {
    private String english;
    private List<PhraseResponseDTO> options;

    public TranslatePhraseItemResponseDTO(String english, List<PhraseResponseDTO> options) {
        super();
        this.english = english;
        this.options = options;
    }
}
