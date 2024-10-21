package org.alouastudios.easytagalogbackend.dto;

import java.util.List;

public record PhraseDTO (
        String tagalog,
        String english,
        Boolean isQuestion,
        List<Long> wordIds, // TODO: Change to UUID
        List<String> wordIdLinkedMeaningConjugationOrder
) {
}
