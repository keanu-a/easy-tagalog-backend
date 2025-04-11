package org.alouastudios.easytagalogbackend.dto.phrase;

public record PhraseWordResponseDTO(
        Integer position,
        String english,
        String note,
        Boolean isProperNoun,
        String audioUrl
) {
}
