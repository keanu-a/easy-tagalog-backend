package org.alouastudios.easytagalogbackend.dto;

import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;
import org.alouastudios.easytagalogbackend.model.word.Conjugation;

import java.util.List;

public record WordDTO (
        Long id,
        String tagalog,
        String root,
        String accents,
        String audioUrl,
        PartOfSpeech partOfSpeech,
        Boolean isIrregularVerb,
        List<Conjugation> conjugations
) {
}
