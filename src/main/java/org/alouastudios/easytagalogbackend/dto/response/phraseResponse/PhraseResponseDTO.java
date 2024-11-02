package org.alouastudios.easytagalogbackend.dto.response.phraseResponse;


import java.util.List;
import java.util.UUID;

public record PhraseResponseDTO(
        UUID uuid,
        String tagalog,
        String english,
        Boolean isQuestion,
        List<String> phraseWordMeanings
) {
}
