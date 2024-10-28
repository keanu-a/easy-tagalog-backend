package org.alouastudios.easytagalogbackend.dto;

import org.alouastudios.easytagalogbackend.enums.QuestionType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record QuestionRequestDTO(
      QuestionType questionType,
      UUID wordId,
      UUID phraseId,
      Set<UUID> wordOptions,
      Set<UUID> phraseOptions,
      UUID correctAnswer,
      List<UUID> correctAnswerOrder,
      String helpInfo
) { }
