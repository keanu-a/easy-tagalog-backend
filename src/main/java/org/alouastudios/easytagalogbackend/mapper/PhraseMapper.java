package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class PhraseMapper {

    public PhraseResponseDTO toResponseDTO(Phrase phrase) {

        return new PhraseResponseDTO(
                phrase.getUuid(),
                phrase.getTagalog(),
                phrase.getEnglish(),
                phrase.getIsQuestion(),
                Arrays.asList(phrase.getPhraseWordMeanings().split(","))
        );
    }

    public void toEntity(
            Phrase phrase,
            PhraseRequestDTO phraseRequest,
            Set<Word> phraseWords,
            List<String> phraseWordMeanings) {

        phrase.setTagalog(phraseRequest.tagalog());
        phrase.setEnglish(phraseRequest.english());
        phrase.setIsQuestion(phraseRequest.isQuestion());
        phrase.setWords(phraseWords);
        phrase.setPhraseWordMeanings(String.join(",", phraseWordMeanings));
    }
}
