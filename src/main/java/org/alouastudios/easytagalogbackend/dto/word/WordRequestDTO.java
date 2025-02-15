package org.alouastudios.easytagalogbackend.dto.word;

import org.alouastudios.easytagalogbackend.model.words.Conjugation;
import org.alouastudios.easytagalogbackend.model.words.LinkedWord;
import org.alouastudios.easytagalogbackend.model.words.Translation;

import java.util.List;
import java.util.Set;

public record WordRequestDTO(
        String tagalog,
        Set<Translation> translations,
        String root,
        List<Integer> accents,
        String alternateSpelling,
        Boolean isIrregularVerb,
        String note,
        Set<Conjugation> conjugations,
        LinkedWord linkedWord,
        String audioUrl
) {
}
