package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.word.WordResponseDTO;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TranslateWordQuestionResponseDTO extends LessonQuestionResponseDTO {

    private List<WordResponseDTO> options;
    private UUID answer;

    public TranslateWordQuestionResponseDTO(String prompt, List<WordResponseDTO> options, UUID answer) {
        super(prompt);
        this.options = options;
        this.answer = answer;
    }
}
