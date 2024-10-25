package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.response.ConjugationResponseDTO;
import org.alouastudios.easytagalogbackend.dto.response.EnglishResponseDTO;
import org.alouastudios.easytagalogbackend.dto.response.LinkedWordResponseDTO;
import org.alouastudios.easytagalogbackend.dto.WordRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.WordResponseDTO;
import org.alouastudios.easytagalogbackend.model.words.Conjugation;
import org.alouastudios.easytagalogbackend.model.words.English;
import org.alouastudios.easytagalogbackend.model.words.LinkedWord;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.util.ServiceUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
                word.getAccents() != null ? ServiceUtil.convertStringToAccentArray(word.getAccents()) : new ArrayList<>(),  // Null check for accents
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
    public Word toEntity(WordRequestDTO wordRequestDTO) {

        // This function only handles initial mapping NOT validation.
        // Validation is done in the service.
        // It excludes fields with entities that need validation:
        //  - english
        //  - conjugations
        //  - linkedWord

        Word newWord = new Word();

        setNonValidatedFields(wordRequestDTO, newWord);

        // Create or set audio url
        if (wordRequestDTO.audioUrl() == null) {
            newWord.setAudioUrl(ServiceUtil.createWordAudioString(wordRequestDTO.tagalog()));
        } else {
            newWord.setAudioUrl(wordRequestDTO.audioUrl());
        }

        // Change accents array to string
        if (wordRequestDTO.accents() != null) {
            newWord.setAccents(ServiceUtil.convertAccentArrayToString(wordRequestDTO.accents()));
        }

        return newWord;
    }

    // This function handles mapping request body data into existing word
    public void updateEntityFromDTO(WordRequestDTO wordRequestDTO, Word existingWord) {

        setNonValidatedFields(wordRequestDTO, existingWord);

        existingWord.setAudioUrl(wordRequestDTO.audioUrl());

        // Change accents array to string
        if (!wordRequestDTO.accents().isEmpty()) {
            existingWord.setAccents(ServiceUtil.convertAccentArrayToString(wordRequestDTO.accents()));
        } else {
            existingWord.setAccents(null);
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

    // This function sets fields that have validation before insertion
    private void setNonValidatedFields(WordRequestDTO wordRequestDTO, Word word) {
        word.setTagalog(wordRequestDTO.tagalog());
        word.setRoot(wordRequestDTO.root());
        word.setPartOfSpeech(wordRequestDTO.partOfSpeech());
        word.setAlternateSpelling(wordRequestDTO.alternateSpelling());
        word.setIsIrregularVerb(wordRequestDTO.isIrregularVerb());
        word.setNote(wordRequestDTO.note());
    }
}
