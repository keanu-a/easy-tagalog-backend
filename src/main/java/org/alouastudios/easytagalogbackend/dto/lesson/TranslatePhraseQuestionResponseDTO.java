package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class TranslatePhraseQuestionResponseDTO extends LessonQuestionResponseDTO {
    private List<PhraseResponseDTO> phrases;
    private UUID answer;
}
