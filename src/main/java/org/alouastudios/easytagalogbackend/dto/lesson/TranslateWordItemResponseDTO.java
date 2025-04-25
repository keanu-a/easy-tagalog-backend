package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.word.WordResponseDTO;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TranslateWordItemResponseDTO extends LessonItemResponseDTO {

    private List<WordResponseDTO> options;
    private UUID answer;

    public TranslateWordItemResponseDTO(List<WordResponseDTO> options, UUID answer) {
        this.options = options;
        this.answer = answer;
    }
}
