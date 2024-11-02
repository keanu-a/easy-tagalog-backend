package org.alouastudios.easytagalogbackend.dto.response.wordResponse;

import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record WordResponseDTO (
        UUID uuid,
        String tagalog,
        Set<EnglishResponseDTO> english,
        String root,
        List<Integer> accents,
        PartOfSpeech partOfSpeech,
        String alternateSpelling,
        Boolean isIrregularVerb,
        String note,
        Set<ConjugationResponseDTO> conjugations,
        LinkedWordResponseDTO linkedWord,
        String audioUrl
) {
}
