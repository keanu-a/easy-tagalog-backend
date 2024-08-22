package org.alouastudios.easytagalogbackend.dto;

import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;
import org.alouastudios.easytagalogbackend.model.word.Conjugation;
import org.alouastudios.easytagalogbackend.model.word.English;

import java.util.Set;

public record WordDTO (
        Long id,
        String tagalog,
        Set<English> english,
        String root,
        String accents,
        PartOfSpeech partOfSpeech,
        String alternateSpelling,
        Boolean isIrregularVerb,
        Set<Conjugation> conjugations,
        String audioUrl
) {
}
