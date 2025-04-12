package org.alouastudios.easytagalogbackend.validator;

import org.alouastudios.easytagalogbackend.dto.word.ConjugationRequestDTO;
import org.alouastudios.easytagalogbackend.dto.word.WordRequestDTO;
import org.alouastudios.easytagalogbackend.enums.Tense;
import org.alouastudios.easytagalogbackend.model.words.Conjugation;
import org.alouastudios.easytagalogbackend.model.words.English;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.repository.EnglishRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class WordValidator {

    public void validateVerb(WordRequestDTO word) {

        // Must provide isIrregularVerb field
        if (word.isIrregularVerb() == null) {
            throw new RuntimeException("Verb must have isIrregularVerb");
        }

        if (word.focusType() == null) {
            throw new RuntimeException("Verb must have focusType");
        }

        // For verbs, check user gave conjugations field
        if (word.conjugations() == null || word.conjugations().isEmpty()) {
            throw new RuntimeException("Verbs must have conjugations");
        }

        // Check user gave 3 conjugations
        if (word.conjugations().size() != 3) {
            throw new RuntimeException("Verbs must have only 3 conjugations");
        }

        // Check user provided PAST, PRESENT, FUTURE tenses

        boolean past = false, present = false, future = false;
        for (ConjugationRequestDTO c : word.conjugations()) {
            if (c.tense() == Tense.PAST) past = true;
            if (c.tense() == Tense.PRESENT) present = true;
            if (c.tense() == Tense.FUTURE) future = true;
        }

        if (!past) throw new RuntimeException("Verb missing past conjugation");
        if (!present) throw new RuntimeException("Verb missing present conjugation");
        if (!future) throw new RuntimeException("Verb missing future conjugation");
    }
}
