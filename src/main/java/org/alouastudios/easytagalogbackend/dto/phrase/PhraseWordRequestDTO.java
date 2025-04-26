package org.alouastudios.easytagalogbackend.dto.phrase;

import org.alouastudios.easytagalogbackend.enums.Tense;

import java.util.UUID;

public record PhraseWordRequestDTO(
        Integer position,
        UUID englishUuid,
        String note,
        Boolean isProperNoun,
        UUID wordUuid,
        Tense tense, // Required if word is a verb
        Boolean useLinkedWord // Optional
) {
}
