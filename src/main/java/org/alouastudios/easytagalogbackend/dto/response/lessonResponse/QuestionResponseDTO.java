package org.alouastudios.easytagalogbackend.dto.response.lessonResponse;

import org.alouastudios.easytagalogbackend.enums.QuestionType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record QuestionResponseDTO(
        QuestionType questionType,
        LessonWordResponseDTO word,
        LessonPhraseResponseDTO phrase,
        Set<LessonWordResponseDTO> wordOptions,
        Set<LessonPhraseResponseDTO> phraseOptions,
        UUID correctAnswer,
        List<UUID> correctAnswerOrder,
        String helpInfo
) {
}
