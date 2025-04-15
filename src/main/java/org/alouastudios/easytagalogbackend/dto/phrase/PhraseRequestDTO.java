package org.alouastudios.easytagalogbackend.dto.phrase;

import java.util.List;

public record PhraseRequestDTO(
        String tagalog,
        String english,
        Boolean isQuestion,
        String audioUrl,
        List<PhraseWordRequestDTO> phraseWords
) {
}
