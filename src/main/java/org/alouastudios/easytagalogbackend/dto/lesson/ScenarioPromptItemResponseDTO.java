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
public class ScenarioPromptItemResponseDTO extends LessonItemResponseDTO {
    private PhraseResponseDTO promptPhrase;
    private List<PhraseResponseDTO> options;

    public ScenarioPromptItemResponseDTO(UUID uuid, PhraseResponseDTO promptPhrase, List<PhraseResponseDTO> options) {
        super(uuid);
        this.promptPhrase = promptPhrase;
        this.options = options;
    }
}
