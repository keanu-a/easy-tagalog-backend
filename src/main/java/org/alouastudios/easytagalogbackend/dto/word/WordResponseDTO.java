package org.alouastudios.easytagalogbackend.dto.word;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record WordResponseDTO (
        UUID uuid,
        String tagalog,
        Set<TranslationResponseDTO> translations,
        String root,
        List<Integer> accents,
        String alternateSpelling,
        Boolean isIrregularVerb,
        String note,
        Set<ConjugationResponseDTO> conjugations,
        LinkedWordResponseDTO linkedWord,
        String audioUrl
) {
}
