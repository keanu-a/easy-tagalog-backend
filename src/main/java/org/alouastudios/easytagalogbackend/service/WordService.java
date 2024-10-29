package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.WordRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.wordResponse.WordResponseDTO;
import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.WordMapper;
import org.alouastudios.easytagalogbackend.model.words.Conjugation;
import org.alouastudios.easytagalogbackend.model.words.English;
import org.alouastudios.easytagalogbackend.model.words.LinkedWord;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.repository.EnglishRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.alouastudios.easytagalogbackend.util.ServiceUtil;
import org.alouastudios.easytagalogbackend.validator.WordValidator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    private final EnglishRepository englishRepository;

    private final WordValidator wordValidator;

    private final WordMapper wordMapper;

    public List<WordResponseDTO> getAllWords() {

        List<Word> foundWords = wordRepository.findAll();
        List<WordResponseDTO> words = new ArrayList<>();

        for (Word word : foundWords) {
            words.add(wordMapper.toResponseDTO(word));
        }

        return words;
    }

    public List<WordResponseDTO> getWordsBySearchQuery(String searchQuery) {

        List<Word> foundWords = wordRepository.findWordsBySearchQuery(searchQuery);
        List<WordResponseDTO> words = new ArrayList<>();

        for (Word word : foundWords) {
            words.add(wordMapper.toResponseDTO(word));
        }

        return words;
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

        List<WordResponseDTO> newWords = new ArrayList<>();

        for (WordRequestDTO wordRequestDTO : words) {
            newWords.add(addWord(wordRequestDTO));
        }

        return newWords;
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

        // First validate and set english
        Set<English> newEnglishSet = getEnglishSet(word, wordRequest.english());

        // Next validate if verb, and set conjugations
        Set<Conjugation> newConjugationSet = null;
        if (wordRequest.partOfSpeech().equals(PartOfSpeech.VERB)) {
            wordValidator.validateVerb(wordRequest);
            newConjugationSet = getConjugationSet(word, wordRequest.conjugations());
        }

        // Next set linkedWord if given
        LinkedWord newLinkedWord = null;
        if (wordRequest.linkedWord() != null) {
            newLinkedWord = getLinkedWord(word, wordRequest.linkedWord());
        }

        // Creates initial mapping of word
        wordMapper.toEntity(word, wordRequest, newEnglishSet, newConjugationSet, newLinkedWord);
    }

    // This function returns a new LinkedWord object with the linked word's word field set
    private LinkedWord getLinkedWord(Word word, LinkedWord requestLinkedWord) {

        LinkedWord newLinkedWord = new LinkedWord();
        newLinkedWord.setTagalog(requestLinkedWord.getTagalog());
        newLinkedWord.setWord(word);
        newLinkedWord.setAudioUrl(ServiceUtil.createWordAudioString(newLinkedWord.getTagalog()));

        return newLinkedWord;
    }

    // This function returns a new english set with english's word field set
    private Set<English> getEnglishSet(Word word, Set<English> englishSet) {

        // Ensures english field is provided
        if (englishSet == null || englishSet.isEmpty()) {
            throw new RuntimeException("Word must have english translations");
        }

        Set<English> newEnglishSet = new HashSet<>();

        // Goes through each english word and creates new English if it doesn't exist or adds current word to existing
        for (English english : englishSet) {
            English foundEnglish = englishRepository.findByMeaning(english.getMeaning());

            if (foundEnglish == null) {
                english.getWords().add(word);
                newEnglishSet.add(english);
            } else {
                foundEnglish.getWords().add(word);
                newEnglishSet.add(foundEnglish);
            }
        }

        return newEnglishSet;
    }

    // This function returns the new conjugation set with the conjugation's word field set
    private Set<Conjugation> getConjugationSet(Word word, Set<Conjugation> conjugationSet) {

        Set<Conjugation> newConjugationSet = new HashSet<>();

        for (Conjugation conjugation : conjugationSet) {
            conjugation.setWord(word);
            newConjugationSet.add(conjugation);
        }

        return newConjugationSet;
    }
}
