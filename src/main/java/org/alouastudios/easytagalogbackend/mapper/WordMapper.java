package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.word.*;
import org.alouastudios.easytagalogbackend.model.words.*;
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
                word.getTranslations().stream()
                        .map(this::toTranslationResponseDTO)
                        .collect(Collectors.toSet()),
                word.getRoot(),
                word.getAccents() != null
                        ? ServiceUtil.convertStringToAccentArray(word.getAccents())
                        : new ArrayList<>(),  // Null check for accents
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
            Set<Translation> translations,
            Set<Conjugation> conjugations,
            LinkedWord linkedWord) {

        word.setTagalog(wordRequestDTO.tagalog());
        word.setRoot(wordRequestDTO.root());
        word.setAlternateSpelling(wordRequestDTO.alternateSpelling());
        word.setIsIrregularVerb(wordRequestDTO.isIrregularVerb());
        word.setNote(wordRequestDTO.note());

        word.setTranslations(translations);
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

    // Maps Translation entity to TranslationDTO
    public TranslationResponseDTO toTranslationResponseDTO(Translation translation) {
        return new TranslationResponseDTO(
                translation.getPartOfSpeech(),
                translation.getEnglishMeanings().stream()
                        .map(this::toEnglishDTO)
                        .collect(Collectors.toSet())
        );
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
