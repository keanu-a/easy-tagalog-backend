package org.alouastudios.easytagalogbackend.validator;

import org.alouastudios.easytagalogbackend.dto.phrase.PhraseRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PhraseValidator {

    public void validatePhraseRequest(PhraseRequestDTO phrase) {
        if (Objects.isNull(phrase)) {
            throw new RuntimeException("Phrase request cannot be null");
        }

        if (phrase.tagalog() == null) {
            throw new RuntimeException("Tagalog cannot be null");
        }

        if (phrase.english() == null) {
            throw new RuntimeException("English cannot be null");
        }

        if (phrase.wordUuids() == null || phrase.wordUuids().isEmpty()) {
            throw new RuntimeException("WordIds cannot be null or empty");
        }

        if (Objects.isNull(phrase.phraseWordGuides())) {
            throw new RuntimeException("Phrase word guides cannot be null");
        }

        // There should be a meaning for each word of the phrase (even names)
        if (phrase.tagalog().split(" ").length != phrase.phraseWordGuides().size()) {
            throw new RuntimeException("Phrase word guide length doesn't match the number of words in the phrase");
        }
    }
}
