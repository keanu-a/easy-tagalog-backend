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

    public List<WordResponseDTO> getWordsBySearchQuery(String searchQuery) {
        return wordRepository.findWordsBySearchQuery(searchQuery)
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

        handleEntityChanges(newWord, wordRequest);

        // Insert new word into the database
        wordRepository.save(newWord);

        // Return mapped DTO
        return wordMapper.toResponseDTO(newWord);
    }

    @Transactional
    public List<WordResponseDTO> addWords(List<WordRequestDTO> words) {

        List<Word> newWords = words.stream()
                .map(word -> {
                    Word newWord = new Word();
                    handleEntityChanges(newWord, word);
                    return newWord;
                })
                .toList();

        wordRepository.saveAll(newWords);

        return newWords.stream()
                .map(wordMapper::toResponseDTO)
                .toList();
    }

    // This function handles updating an existing word in the database
    public WordResponseDTO updateWord(UUID uuid, WordRequestDTO wordRequest) {

        Word foundWord = wordRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        handleEntityChanges(foundWord, wordRequest);
        wordRepository.save(foundWord);

        return wordMapper.toResponseDTO(foundWord);
    }

    public void deleteWord(UUID uuid) {
        Word foundWord = wordRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        wordRepository.delete(foundWord);
    }

    // This function handles changes to a Word entity
    private void handleEntityChanges(Word word, WordRequestDTO wordRequest) {

        // First validate if verb, and set conjugations
        Set<Conjugation> newConjugationSet = null;
        boolean isVerb = wordRequest.translations().stream().anyMatch(t -> t.getPartOfSpeech() == PartOfSpeech.VERB);
        if (isVerb) {
            wordValidator.validateVerb(wordRequest);
            newConjugationSet = getConjugationSet(word, wordRequest.conjugations());
        }

        // Next validate and set translations
        Set<Translation> newTranslationSet = getTranslationSet(word, wordRequest.translations());

        // Next set linkedWord if given
        LinkedWord newLinkedWord = null;
        if (wordRequest.linkedWord() != null) {
            newLinkedWord = getLinkedWord(word, wordRequest.linkedWord());
        }

        // Creates initial mapping of word
        wordMapper.toEntity(word, wordRequest, newTranslationSet, newConjugationSet, newLinkedWord);
    }

    // This function returns a new LinkedWord object with the linked word's word field set
    private LinkedWord getLinkedWord(Word word, LinkedWord requestLinkedWord) {

        LinkedWord newLinkedWord = new LinkedWord();
        newLinkedWord.setTagalog(requestLinkedWord.getTagalog());
        newLinkedWord.setWord(word);
        newLinkedWord.setAudioUrl(ServiceUtil.createWordAudioString(newLinkedWord.getTagalog()));

        return newLinkedWord;
    }

    // This function returns a new Translation set with the translation's word field set
    private Set<Translation> getTranslationSet(Word word, Set<Translation> translationSet) {

        // Ensures translation field is provided
        if (translationSet == null || translationSet.isEmpty()) {
            throw new RuntimeException("Word must have translations");
        }

        Set<Translation> newTranslationSet = new HashSet<>();

        // Creates a set of string english meanings
        Set<String> meanings = translationSet.stream()
                .flatMap(t -> t.getEnglishMeanings().stream().map(English::getMeaning))
                .collect(Collectors.toSet());

        // Queries for all English with those meanings
        Map<String, English> existingEnglishMap = englishRepository.findByMeaningIn(meanings)
                .stream().collect(Collectors.toMap(English::getMeaning, Function.identity()));

        // Iterates through translation set and sets the bidirectional relationship between Translation and English
        for (Translation translation : translationSet) {
            Set<English> newEnglishSet = new HashSet<>();

            if (translation.getEnglishMeanings() == null || translation.getEnglishMeanings().isEmpty()) {
                throw new RuntimeException("Translation must have english meanings");
            }

            for (English english : translation.getEnglishMeanings()) {
                English foundEnglish = existingEnglishMap.get(english.getMeaning());

                if (foundEnglish == null) {
                    english.getTranslations().add(translation);
                    newEnglishSet.add(english);
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

    // This function returns the new conjugation set with the conjugation's word field set
    private Set<Conjugation> getConjugationSet(Word word, Set<Conjugation> conjugationSet) {
        return conjugationSet.stream()
                .peek(conjugation -> conjugation.setWord(word))
                .collect(Collectors.toSet());
    }
}
