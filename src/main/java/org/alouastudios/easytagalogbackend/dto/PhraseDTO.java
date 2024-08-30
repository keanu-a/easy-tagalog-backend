package org.alouastudios.easytagalogbackend.dto;

import java.util.List;

public record PhraseDTO (
        String tagalog,
        String english,
        List<Long> wordIds,
        String wordOrder
) {
}
