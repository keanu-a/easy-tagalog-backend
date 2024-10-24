package org.alouastudios.easytagalogbackend.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record PhraseRequestDTO(
        String tagalog,
        String english,
        Boolean isQuestion,
        Set<UUID> wordIds,
        List<PhraseWordOrderDTO> phraseWordOrder
) {
}
