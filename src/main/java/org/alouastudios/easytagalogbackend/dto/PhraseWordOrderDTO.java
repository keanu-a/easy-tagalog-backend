package org.alouastudios.easytagalogbackend.dto;

import org.alouastudios.easytagalogbackend.enums.Tense;

import java.util.UUID;

public record PhraseWordOrderDTO(
        UUID id,
        Boolean isLinked,
        UUID englishId,
        Tense tense
) {
}
