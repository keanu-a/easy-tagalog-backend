package org.alouastudios.easytagalogbackend.validator;

import org.alouastudios.easytagalogbackend.util.ServiceUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PhraseValidator {

    public void validateWordIdMeaningConjugationOrder(List<String> order) {

        // Examples of correct notation for wordIdMeaningConjugationOrder:
        // 1:-:-
        // 4:7:-
        // 6:-:PAST
        // 19:6:PRESENT

        for (String word : order) {

            String[] orderParts = word.split(":");

            // REQUIRED - Must be of length 3
            if (orderParts.length != 3) {
                throw new RuntimeException("Each word order must be separated by 2 colons");
            }

            // REQUIRED - First order: Has to be number (Word id)
            if (!ServiceUtil.isNumber(orderParts[0])) {
                throw new RuntimeException("First part of the order must be a number");
            }

            // OPTIONAL - Second Order: If provided, has to be number (English id)
            if (!Objects.equals(orderParts[1], "-") && !ServiceUtil.isNumber(orderParts[1])) {
                throw new RuntimeException("Second part of the order must be a number");
            }

            // OPTIONAL - Third order: If provided, has to be of PAST, PRESENT, FUTURE
            String tense = orderParts[2].toUpperCase();
            if (!Objects.equals(tense, "-") && !ServiceUtil.isTense(tense)) {
                throw new RuntimeException("Third part must be tense: PAST, PRESENT, OR FUTURE");
            }
        }
    }
}
