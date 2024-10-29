package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.response.wordResponse.ConjugationResponseDTO;
import org.alouastudios.easytagalogbackend.dto.response.wordResponse.EnglishResponseDTO;
import org.alouastudios.easytagalogbackend.dto.response.wordResponse.LinkedWordResponseDTO;
import org.alouastudios.easytagalogbackend.dto.WordRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.wordResponse.WordResponseDTO;
import org.alouastudios.easytagalogbackend.model.words.Conjugation;
import org.alouastudios.easytagalogbackend.model.words.English;
import org.alouastudios.easytagalogbackend.model.words.LinkedWord;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.util.ServiceUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WordMapper {

    // Maps the Word entity to WordResponseDTO
    public WordResponseDTO toResponseDTO(Word word) {
        return new WordResponseDTO(
                word.getUuid(),
                word.getTagalog(),
                word.getEnglish().stream().map(this::toEnglishDTO).collect(Collectors.toSet()),
                word.getRoot(),
                word.getAccents() != null
                        ? ServiceUtil.convertStringToAccentArray(word.getAccents())
                        : new ArrayList<>(),  // Null check for accents
                word.getPartOfSpeech(),
                word.getAlternateSpelling(),
                word.getIsIrregularVerb(),
                word.getNote(),
                !word.getConjugations().isEmpty()
                        ? word.getConjugations().stream().map(this::toConjugationDTO).collect(Collectors.toSet())
                        : null,
                word.getLinkedWord() != null ? this.toLinkedWordDTO(word.getLinkedWord()) : null,
                word.getAudioUrl()
        );
    }

    // Maps the WordRequestDTO to new Word Entity
    public void toEntity(
            Word word,
            WordRequestDTO wordRequestDTO,
            Set<English> english,
            Set<Conjugation> conjugations,
            LinkedWord linkedWord) {

        word.setTagalog(wordRequestDTO.tagalog());
        word.setRoot(wordRequestDTO.root());
        word.setPartOfSpeech(wordRequestDTO.partOfSpeech());
        word.setAlternateSpelling(wordRequestDTO.alternateSpelling());
        word.setIsIrregularVerb(wordRequestDTO.isIrregularVerb());
        word.setNote(wordRequestDTO.note());

        word.setEnglish(english);
        if (conjugations != null) word.setConjugations(conjugations);
        if (linkedWord != null) word.setLinkedWord(linkedWord);

        // Create or set audio url
        if (wordRequestDTO.audioUrl() == null) {
            word.setAudioUrl(ServiceUtil.createWordAudioString(wordRequestDTO.tagalog()));
        } else {
            word.setAudioUrl(wordRequestDTO.audioUrl());
        }

        // Change accents array to string
        if (wordRequestDTO.accents() != null) {
            word.setAccents(ServiceUtil.convertAccentArrayToString(wordRequestDTO.accents()));
        }
    }

    // Maps English entity to EnglishDTO
    public EnglishResponseDTO toEnglishDTO(English english) {
        return new EnglishResponseDTO(
                english.getUuid(),
                english.getMeaning()
        );
    }

    // Maps Conjugation entity to ConjugationDTO
    public ConjugationResponseDTO toConjugationDTO(Conjugation conjugation) {
        return new ConjugationResponseDTO(
                conjugation.getTagalog(),
                conjugation.getRoot(),
                ServiceUtil.convertStringToAccentArray(conjugation.getAccents()),
                conjugation.getAudioUrl(),
                conjugation.getEnglish(),
                conjugation.getTense()
        );
    }

    // Maps LinkedWord entity to LinkedWordDTO
    public LinkedWordResponseDTO toLinkedWordDTO(LinkedWord linkedWord) {
        return new LinkedWordResponseDTO(
                linkedWord.getTagalog(),
                linkedWord.getAudioUrl()
        );
    }
}
