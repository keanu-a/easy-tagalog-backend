package org.alouastudios.easytagalogbackend.dto;

import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;
import org.alouastudios.easytagalogbackend.model.words.Conjugation;
import org.alouastudios.easytagalogbackend.model.words.English;
import org.alouastudios.easytagalogbackend.model.words.LinkedWord;

import java.util.List;
import java.util.Set;

public record WordRequestDTO(
        String tagalog,
        Set<English> english,
        String root,
        List<Integer> accents,
        PartOfSpeech partOfSpeech,
        String alternateSpelling,
        Boolean isIrregularVerb,
        String note,
        Set<Conjugation> conjugations,
        LinkedWord linkedWord,
        String audioUrl
) {
}
