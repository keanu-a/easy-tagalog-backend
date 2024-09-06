package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.WordDTO;
import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;
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

    public Word addWord(WordDTO word) {

        Word newWord = new Word();

        newWord.setTagalog(word.tagalog());

        // Ensures english field is provided
        if (word.english() == null || word.english().isEmpty()) {
            throw new RuntimeException("Word must have english translations");
        }

        // Validates english field
        Set<English> englishSet = wordValidator.validateEnglish(word, newWord, englishRepository);
        newWord.setEnglish(englishSet);

        newWord.setRoot(word.root());
        newWord.setPartOfSpeech(word.partOfSpeech());
        newWord.setAlternateSpelling(word.alternateSpelling());
        newWord.setIsIrregularVerb(word.isIrregularVerb());
        newWord.setNote(word.note());

        // Create or set audio url
        if (word.audioUrl() == null) {
            newWord.setAudioUrl(ServiceUtil.createWordAudioString(word.tagalog()));
        } else {
            newWord.setAudioUrl(word.audioUrl());
        }

        // Change accents array to string
        if (word.accents() != null) {
            newWord.setAccents(ServiceUtil.convertAccentArrayToString(word.accents()));
        }

        // Validate checks for verbs
        if (word.partOfSpeech() == PartOfSpeech.VERB) {

            wordValidator.validateVerb(word);

            // If passed all checks, set conjugation to word
            for (Conjugation c : word.conjugations()) {
                c.setWord(newWord);
            }
        }

        // Setting the linked word's word field
        if (word.linkedWord() != null) {
            LinkedWord newLinkedWord = word.linkedWord();
            newLinkedWord.setWord(newWord);
            newLinkedWord.setAudioUrl(ServiceUtil.createWordAudioString(newLinkedWord.getTagalog()));
            newWord.setLinkedWord(newLinkedWord);
        }

        return wordRepository.save(newWord);
    }

    @Transactional
    public List<Word> addWords(List<WordDTO> words) {

        List<Word> newWords = new ArrayList<>();

        for (WordDTO wordDTO : words) {
            newWords.add(addWord(wordDTO));
        }

        return newWords;
    }

    public Word updateWord(Word word) {
        return wordRepository.save(word);
    }

    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }
}
