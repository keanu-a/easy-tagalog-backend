package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alouastudios.easytagalogbackend.dto.word.WordResponseDTO;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class TranslateWordQuestionResponseDTO extends LessonQuestionResponseDTO {
    private List<WordResponseDTO> options;
    private UUID answer;
}
