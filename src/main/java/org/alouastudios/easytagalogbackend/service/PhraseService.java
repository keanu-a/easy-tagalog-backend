package org.alouastudios.easytagalogbackend.service;

import org.alouastudios.easytagalogbackend.dto.PhraseDTO;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.repository.PhraseRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhraseService {

    private final PhraseRepository phraseRepository;

    private final WordRepository wordRepository;

    public PhraseService(PhraseRepository phraseRepository, WordRepository wordRepository) {
        this.phraseRepository = phraseRepository;
        this.wordRepository = wordRepository;
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
        newPhrase.setWordOrder(phrase.wordOrder());
        newPhrase.setEnglish(phrase.english());

        List<Word> words = wordRepository.findAllByIdIn(phrase.wordIds());

        if (words.size() != phrase.wordIds().size()) {
            throw new RuntimeException("Some Word IDs do not exists");
        }

        newPhrase.setWords(words);

        return phraseRepository.save(newPhrase);
    }
}
