package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TranslatePhraseQuestionResponseDTO extends LessonQuestionResponseDTO {
    private List<PhraseResponseDTO> options;
    private UUID answer;

    public TranslatePhraseQuestionResponseDTO(List<PhraseResponseDTO> options, UUID answer) {
        super();
        this.options = options;
        this.answer = answer;
    }
}
