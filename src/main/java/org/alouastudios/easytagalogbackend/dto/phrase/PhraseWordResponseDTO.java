package org.alouastudios.easytagalogbackend.dto.phrase;

public record PhraseWordResponseDTO(
        Integer position,
        String englishMeaning,
        String note,
        Boolean isProperNoun
) {
}
