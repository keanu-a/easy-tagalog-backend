package org.alouastudios.easytagalogbackend.dto.response;

import org.alouastudios.easytagalogbackend.enums.QuestionType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record QuestionResponseDTO(
        QuestionType questionType,
        WordResponseDTO word,
        PhraseResponseDTO phrase,
        Set<WordResponseDTO> wordOptions,
        Set<PhraseResponseDTO> phraseOptions,
        UUID correctAnswer,
        List<UUID> correctAnswerOrder,
        String helpInfo
) {
}
