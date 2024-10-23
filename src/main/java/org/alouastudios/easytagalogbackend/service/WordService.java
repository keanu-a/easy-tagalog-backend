package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.WordRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.WordResponseDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    public WordResponseDTO addWord(WordRequestDTO word) {

        // Creates initial mapping of word
        Word wordEntity = wordMapper.toEntity(word);

        // Check fields that NEED validation and map the rest
        validateWordInput(word, wordEntity);

        // Insert new word into the database
        wordRepository.save(wordEntity);

        // Return mapped DTO
        return wordMapper.toResponseDTO(wordEntity);
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
    public WordResponseDTO updateWord(UUID uuid, WordRequestDTO word) {

        Word foundWord = wordRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        // Maps fields that NEED validation first
        validateWordInput(word, foundWord);

        // Map the rest of the fields
        wordMapper.updateEntityFromDTO(word, foundWord);

        // Save word into database
        Word updatedWord = wordRepository.save(foundWord);

        // Return DTO of updated word
        return wordMapper.toResponseDTO(updatedWord);
    }

    public void deleteWord(UUID uuid) {
        Word foundWord = wordRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        wordRepository.delete(foundWord);
    }

    // This function checks fields that NEED to be validated
    private void validateWordInput(WordRequestDTO word, Word wordEntity) {

        // Ensures english field is provided
        if (word.english() == null || word.english().isEmpty()) {
            throw new RuntimeException("Word must have english translations");
        }

        // Validates english field
        Set<English> englishSet = wordValidator.validateEnglish(word, wordEntity, englishRepository);
        wordEntity.setEnglish(englishSet);

        // Validate checks for verbs
        if (word.partOfSpeech() == PartOfSpeech.VERB) {

            wordValidator.validateVerb(word);

            // If passed all checks, set conjugation to word
            for (Conjugation c : word.conjugations()) {
                c.setWord(wordEntity);
            }
        }

        // Setting the linked word's word field
        if (word.linkedWord() != null) {
            LinkedWord newLinkedWord = word.linkedWord();
            newLinkedWord.setWord(wordEntity);
            newLinkedWord.setAudioUrl(ServiceUtil.createWordAudioString(newLinkedWord.getTagalog()));
            wordEntity.setLinkedWord(newLinkedWord);
        }
    }
}
