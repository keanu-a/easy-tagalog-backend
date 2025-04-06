package org.alouastudios.easytagalogbackend.dto.phrase;

import java.util.UUID;

public record PhraseWordRequestDTO(
        Integer position,
        String english,
        String note,
        Boolean isProperNoun,
        UUID wordUuid
) {
}
