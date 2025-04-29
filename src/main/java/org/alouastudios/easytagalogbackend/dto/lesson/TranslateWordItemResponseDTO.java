package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.word.WordResponseDTO;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TranslateWordItemResponseDTO extends LessonItemResponseDTO {
    private String english;
    private List<WordResponseDTO> options;
    private UUID answer;

    public TranslateWordItemResponseDTO(String english, List<WordResponseDTO> options, UUID answer) {
        super();
        this.english = english;
        this.options = options;
        this.answer = answer;
    }
}
