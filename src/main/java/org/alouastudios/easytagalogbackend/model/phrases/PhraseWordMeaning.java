package org.alouastudios.easytagalogbackend.model.phrases;

import org.alouastudios.easytagalogbackend.enums.Tense;

import java.util.UUID;

public record PhraseWordMeaning(
        UUID id,
        Boolean isLinked,
        UUID englishId,
        Tense tense,
        Boolean nonDatabaseWord
) {

    // nonDatabaseWord default is false
    public PhraseWordMeaning(UUID id, Boolean isLinked, UUID englishId, Tense tense) {
        this(id, isLinked, englishId, tense, false);
    }
}
