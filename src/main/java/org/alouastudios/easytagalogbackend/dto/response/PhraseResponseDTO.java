package org.alouastudios.easytagalogbackend.dto.response;


import java.util.List;

public record PhraseResponseDTO(
        String tagalog,
        String english,
        Boolean isQuestion,
        String[] phraseWordMeanings
) {
}
