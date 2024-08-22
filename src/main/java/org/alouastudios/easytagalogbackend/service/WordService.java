package org.alouastudios.easytagalogbackend.service;

import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;
import org.alouastudios.easytagalogbackend.model.word.Conjugation;
import org.alouastudios.easytagalogbackend.model.word.English;
import org.alouastudios.easytagalogbackend.model.word.Word;
import org.alouastudios.easytagalogbackend.repository.EnglishRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.alouastudios.easytagalogbackend.validator.WordValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    private final EnglishRepository englishRepository;

    private final WordValidator wordValidator;

    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }

    public List<Word> getWordsBySearchQuery(String searchQuery) {
        return wordRepository.findWordsBySearchQuery(searchQuery);
    }

    public Word getWordById(Long id) {
        return wordRepository.findById(id).orElse(null);
    }

    public Word addWord(Word word) {

        // Check for "english" array field
        if (word.getEnglish() == null || word.getEnglish().isEmpty()) {
            throw new RuntimeException("Word must have english translations");
        }

        // Checks if word is of partOfSpeech type VERB
        if (word.getPartOfSpeech() == PartOfSpeech.VERB) {

            wordValidator.validateVerb(word);

            // If passed all checks, set conjugation to word
            for (Conjugation c : word.getConjugations()) {
                c.setWord(word);
            }
        }

        // Check if english meaning already exists
        Set<English> englishSet = wordValidator.validateEnglish(word, englishRepository);

        // Set validated english set
        word.setEnglish(englishSet);

        return wordRepository.save(word);
    }

    public Word updateWord(Word word) {
        return wordRepository.save(word);
    }

    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }
}
