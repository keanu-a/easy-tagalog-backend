package org.alouastudios.easytagalogbackend.dto.response;

import org.alouastudios.easytagalogbackend.enums.Tense;

import java.util.List;

public record ConjugationResponseDTO(
        String tagalog,
        String root,
        List<Integer> accents,
        String audioUrl,
        String english,
        Tense tense
) {
}
