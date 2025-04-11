package org.alouastudios.easytagalogbackend.dto.word;

import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;

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
        LinkedWordDTO linkedWord,
        String audioUrl,
        List<PhraseResponseDTO> examplePhrases
) {
}
