package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.phrase.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseWordResponseDTO;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.phrases.PhraseWord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhraseMapper {

    // Maps a Phrase entity to the PhraseResponseDTO
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

    // Maps phrase request to Phrase entity object
    public void toEntity(
            Phrase phrase,
            PhraseRequestDTO phraseRequestDTO,
            List<PhraseWord> phraseWords) {
        phrase.setTagalog(phraseRequestDTO.tagalog());
        phrase.setEnglish(phraseRequestDTO.english());
        phrase.setIsQuestion(phraseRequestDTO.isQuestion());
        phrase.setPhraseWords(phraseWords);
    }
}
