package org.alouastudios.easytagalogbackend.service;

import org.alouastudios.easytagalogbackend.dto.PhraseDTO;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.repository.PhraseRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.alouastudios.easytagalogbackend.util.ServiceUtil;
import org.alouastudios.easytagalogbackend.validator.PhraseValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhraseService {

    private final PhraseRepository phraseRepository;

    private final WordRepository wordRepository;

    private final PhraseValidator phraseValidator;

    public PhraseService(PhraseRepository phraseRepository, WordRepository wordRepository, PhraseValidator phraseValidator) {
        this.phraseRepository = phraseRepository;
        this.wordRepository = wordRepository;
        this.phraseValidator = phraseValidator;
    }

    public List<Phrase> getAllPhrases() {
        return phraseRepository.findAll();
    }

    public Phrase getPhraseById(Long id) {
        return phraseRepository.findById(id).orElse(null);
    }

    public Phrase addPhrase(PhraseDTO phrase) {

        Phrase newPhrase = new Phrase();
        newPhrase.setTagalog(phrase.tagalog());
        newPhrase.setEnglish(phrase.english());

        // Validates notation
        phraseValidator.validateWordIdMeaningConjugationOrder(phrase.wordIdMeaningConjugationOrder());

        // If validation passed, convert to string to store into database
        String wordOrder = ServiceUtil.convertOrderArrayToString(phrase.wordIdMeaningConjugationOrder());
        newPhrase.setWordIdMeaningConjugationOrder(wordOrder);

        // Validates Word IDs
        List<Word> words = wordRepository.findAllByIdIn(phrase.wordIds());
        if (words.size() != phrase.wordIds().size()) {
            throw new RuntimeException("Some Word IDs do not exists");
        }

        newPhrase.setWords(words);

        return phraseRepository.save(newPhrase);
    }
}
