package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.word.WordRequestDTO;
import org.alouastudios.easytagalogbackend.dto.word.WordResponseDTO;
import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.WordMapper;
import org.alouastudios.easytagalogbackend.model.words.*;
import org.alouastudios.easytagalogbackend.repository.EnglishRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.alouastudios.easytagalogbackend.util.ServiceUtil;
import org.alouastudios.easytagalogbackend.validator.WordValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final EnglishRepository englishRepository;

    private final WordValidator wordValidator;

    private final WordMapper wordMapper;

    public List<WordResponseDTO> getAllWords() {
        return wordRepository.findAll()
                .stream()
                .map(wordMapper::toResponseDTO)
                .toList();
    }

    public WordResponseDTO getWordByUUID(UUID uuid) {
        Word foundWord = wordRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        return wordMapper.toResponseDTO(foundWord);
    }

    @Transactional
    public WordResponseDTO addWord(WordRequestDTO wordRequest) {

        Word newWord = new Word();

        handleWordChanges(newWord, wordRequest);

        // Insert new word into the database
        wordRepository.save(newWord);

        // Return mapped DTO
        return wordMapper.toResponseDTO(newWord);
    }

    @Transactional
    public List<WordResponseDTO> addWords(List<WordRequestDTO> words) {

        List<WordResponseDTO> newWords = new ArrayList<>();

        for (WordRequestDTO wordRequest : words) {
            newWords.add(addWord(wordRequest));
        }

        return newWords;
    }

    // This function handles updating an existing word in the database
    public WordResponseDTO updateWord(UUID uuid, WordRequestDTO wordRequest) {

        Word foundWord = wordRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        handleWordChanges(foundWord, wordRequest);

        // Save updated word to the database
        wordRepository.save(foundWord);

        return wordMapper.toResponseDTO(foundWord);
    }

    public void deleteWord(UUID uuid) {
        Word foundWord = wordRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        wordRepository.delete(foundWord);
    }

    public List<WordResponseDTO> getWordsBySearchQuery(String searchQuery) {
        return wordRepository.findWordsBySearchQuery(searchQuery)
                .stream()
                .map(wordMapper::toResponseDTO)
                .toList();
    }

    /**
     * Handles the process of transforming a WordRequestDTO into an updated Word entity.
     * Includes verb validation, linked word resolution, and setting all core fields.
     */
    private void handleWordChanges(Word word, WordRequestDTO wordRequest) {
        // Check if word is a verb by checking its translations
        boolean isVerb = wordRequest.translations().stream()
                .anyMatch(translation -> translation.getPartOfSpeech() == PartOfSpeech.VERB);

        // First validate if verb, and set conjugations
        Set<Conjugation> newConjugationSet = null;
        if (isVerb) {
            wordValidator.validateVerb(wordRequest);
            newConjugationSet = wordMapper.toConjugationEntity(word, wordRequest.conjugations());

            // Setting default conjugation audio url
            for (Conjugation conjugation : newConjugationSet) {
                if (conjugation.getAudioUrl() == null || conjugation.getAudioUrl().isBlank()) {
                    conjugation.setAudioUrl(ServiceUtil.createWordAudioString(conjugation.getTagalog()));
                }
            }
        }

        // Process and set translations
        Set<Translation> newTranslationSet = getTranslations(word, wordRequest.translations());

        // Process and set linked word if provided
        LinkedWord newLinkedWord = null;
        if (wordRequest.linkedWord() != null) {
            // If the word already has a linked word, update that instance
            LinkedWord linkedWordEntity = word.getLinkedWord() != null
                    ? word.getLinkedWord()
                    : new LinkedWord(); // for POSTs (new words)

            newLinkedWord = wordMapper.toLinkedWordEntity(word, wordRequest.linkedWord(), linkedWordEntity);

            // Setting default linked word audio url
            if (newLinkedWord.getAudioUrl() == null || newLinkedWord.getAudioUrl().isBlank()) {
                newLinkedWord.setAudioUrl(ServiceUtil.createWordAudioString(newLinkedWord.getTagalog()));
            }
        }

        // Lastly set word audio url
        if (wordRequest.audioUrl() == null || wordRequest.audioUrl().isBlank()) {
            word.setAudioUrl(ServiceUtil.createWordAudioString(wordRequest.tagalog()));
        }

        // Creates initial mapping of word
        wordMapper.toEntity(word, wordRequest, newTranslationSet, newConjugationSet, newLinkedWord);
    }

    // This function returns a new Translation set with the translation's word field set
    private Set<Translation> getTranslations(Word word, Set<Translation> translationSet) {

        // Ensures translation field is provided
        if (translationSet == null || translationSet.isEmpty()) {
            throw new RuntimeException("Word must have translations");
        }

        Set<Translation> newTranslationSet = new HashSet<>();

        // Creates a set of string english meanings
        Set<String> meanings = translationSet.stream()
                .flatMap(t -> t.getEnglishMeanings().stream().map(e -> e.getMeaning().toLowerCase()))
                .collect(Collectors.toSet());

        // Queries for all English with those meanings
        Map<String, English> existingEnglishMap = englishRepository.findByMeaningIn(meanings)
                .stream()
                .collect(Collectors.toMap(e -> e.getMeaning().toLowerCase(), Function.identity()));

        // Iterates through translation set and sets the bidirectional relationship between Translation and English
        for (Translation translation : translationSet) {
            Set<English> newEnglishSet = new HashSet<>();

            if (translation.getEnglishMeanings() == null || translation.getEnglishMeanings().isEmpty()) {
                throw new RuntimeException("Translation must have english meanings");
            }

            for (English english : translation.getEnglishMeanings()) {
                String meaningLower = english.getMeaning().toLowerCase();
                English foundEnglish = existingEnglishMap.get(meaningLower);

                if (foundEnglish == null) {
                    English newEnglish = new English();
                    newEnglish.setMeaning(meaningLower);
                    newEnglish.getTranslations().add(translation);
                    newEnglishSet.add(newEnglish);
                } else {
                    foundEnglish.getTranslations().add(translation);
                    newEnglishSet.add(foundEnglish);
                }
            }

            translation.setEnglishMeanings(newEnglishSet);
            translation.setWord(word);
            newTranslationSet.add(translation);
        }

        return newTranslationSet;
    }
}
