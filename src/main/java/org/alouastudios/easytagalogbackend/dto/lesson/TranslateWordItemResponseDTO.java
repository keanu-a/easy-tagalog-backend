package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.word.WordResponseDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TranslateWordItemResponseDTO extends LessonItemResponseDTO {

    private List<WordResponseDTO> options;

    public TranslateWordItemResponseDTO(List<WordResponseDTO> options) {
        this.options = options;
    }
}
