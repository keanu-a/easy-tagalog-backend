package org.alouastudios.easytagalogbackend.dto.lesson;

import org.alouastudios.easytagalogbackend.enums.QuestionType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record QuestionResponseDTO(
        QuestionType questionType,
        LessonPromptResponseDTO prompt,
        Set<LessonOptionResponseDTO> options,
        UUID correctAnswer,
        List<UUID> correctAnswerOrder,
        String helpInfo
) {
}
