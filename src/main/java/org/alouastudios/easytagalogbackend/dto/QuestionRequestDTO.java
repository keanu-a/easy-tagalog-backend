package org.alouastudios.easytagalogbackend.dto;

import org.alouastudios.easytagalogbackend.enums.QuestionType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record QuestionRequestDTO(
      QuestionType questionType,
      UUID wordId,                          // Needs to be mapped
      UUID phraseId,                        // Needs to be mapped
      Set<UUID> wordOptions,                // Needs to be mapped
      Set<UUID> phraseOptions,              // Needs to be mapped
      UUID correctAnswer,                   // DONT map
      List<UUID> correctAnswerOrder,        // DONT map
      String helpInfo
) { }
