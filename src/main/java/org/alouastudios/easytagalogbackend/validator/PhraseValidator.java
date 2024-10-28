package org.alouastudios.easytagalogbackend.validator;

import org.alouastudios.easytagalogbackend.dto.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.util.ServiceUtil;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public void validatePhraseWordMeaning() {}

    public void validatePhraseWordOrder(List<String> order) {

        // Examples of correct notation for wordIdMeaningConjugationOrder:
        // 1:+:-:-
        // 4:-:7:-
        // 6:-:-:PAST
        // 19:+:6:PRESENT

        for (String word : order) {

            String[] orderParts = word.split(":");

            // REQUIRED - Must be of length 3
            if (orderParts.length != 4) {
                throw new RuntimeException("Each word order must be separated by 2 colons");
            }

            // REQUIRED - First order: Has to be number (Word id)
            if (!ServiceUtil.isNumber(orderParts[0])) {
                throw new RuntimeException("First part of the order must be a number");
            }

            // OPTIONAL - Second Order: If provided, marks true for using linked word
            if (!Objects.equals(orderParts[1], "-") && !Objects.equals(orderParts[1], "+")) {
                throw new RuntimeException("Second part of the order must be '-' or '+'");
            }

            // OPTIONAL - Second Order: If provided, has to be number (English id)
            if (!Objects.equals(orderParts[2], "-") && !ServiceUtil.isNumber(orderParts[2])) {
                throw new RuntimeException("Second part of the order must be a number");
            }

            // OPTIONAL - Third order: If provided, has to be of PAST, PRESENT, FUTURE
            String tense = orderParts[3].toUpperCase();
            if (!Objects.equals(tense, "-") && !ServiceUtil.isTense(tense)) {
                throw new RuntimeException("Third part must be tense: PAST, PRESENT, OR FUTURE");
            }
        }
    }
}
