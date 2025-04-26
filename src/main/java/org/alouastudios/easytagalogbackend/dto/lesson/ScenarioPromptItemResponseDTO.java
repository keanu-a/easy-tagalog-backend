package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ScenarioPromptItemResponseDTO extends LessonItemResponseDTO {
    private PhraseResponseDTO promptPhrase;
    private List<PhraseResponseDTO> options;

    public ScenarioPromptItemResponseDTO(PhraseResponseDTO promptPhrase, List<PhraseResponseDTO> options) {
        super();
        this.promptPhrase = promptPhrase;
        this.options = options;
    }
}
