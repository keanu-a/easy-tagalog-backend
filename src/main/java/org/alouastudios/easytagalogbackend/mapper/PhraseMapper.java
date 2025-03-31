package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseWordResponseDTO;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.springframework.stereotype.Component;

@Component
public class PhraseMapper {

    // This maps a Phrase entity to the PhraseResponseDTO
    public PhraseResponseDTO toResponseDTO(Phrase phrase) {

        return new PhraseResponseDTO(
                phrase.getUuid(),
                phrase.getTagalog(),
                phrase.getEnglish(),
                phrase.getIsQuestion(),
                phrase.getPhraseWords().stream().map(pw -> new PhraseWordResponseDTO(
                        pw.getPosition(),
                        pw.getEnglishMeaning(),
                        pw.getNote(),
                        pw.getIsProperNoun()
                )).toList()
        );
    }
}
