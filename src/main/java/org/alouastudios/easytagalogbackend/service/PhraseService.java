package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import org.alouastudios.easytagalogbackend.dto.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.repository.PhraseRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.alouastudios.easytagalogbackend.util.ServiceUtil;
import org.alouastudios.easytagalogbackend.validator.PhraseValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Phrase addPhrase(PhraseRequestDTO phrase) {

        // TODO 10/23: Fix PhraseDTOs logic

        Phrase newPhrase = new Phrase();
        newPhrase.setTagalog(phrase.tagalog());
        newPhrase.setEnglish(phrase.english());

        newPhrase.setIsQuestion(phrase.isQuestion() != null && phrase.isQuestion());

        // Validates notation
        phraseValidator.validateWordIdLinkedMeaningConjugationOrder(phrase.phraseWordOrder());

        // If validation passed, convert to string to store into database
        String wordOrder = ServiceUtil.convertOrderArrayToString(phrase.phraseWordOrder());
        newPhrase.setPhraseWordOrder(wordOrder);

        // Validates Word IDs
//        List<Word> words = wordRepository.findAllByIdIn(phrase.wordIds());

//        // Some phrases will have blanks (ex: <person-name>, <origin-name>, <age>, etc.)
//        List<Long> wordIdsWithoutBlanks = new ArrayList<>();
//        for (Long id : phrase.wordIds()) {
//            if (id != -1) wordIdsWithoutBlanks.add(id);
//        }
//
//        // Comparing found word ids with word ID array without blanks
//        if (words.size() != wordIdsWithoutBlanks.size()) {
//            throw new RuntimeException("Some Word IDs do not exists in " + phrase.tagalog());
//        }
//
//        newPhrase.setWords(words);

        return phraseRepository.save(newPhrase);
    }

    @Transactional
    public List<Phrase> addPhrases(List<PhraseRequestDTO> phrases) {

        List<Phrase> newPhrases = new ArrayList<Phrase>();

        for (PhraseRequestDTO phraseRequestDTO : phrases) {
            newPhrases.add(addPhrase(phraseRequestDTO));
        }

        return newPhrases;
    }

    public Phrase updatePhrase(long id, PhraseRequestDTO phraseRequestDTO) {
        Phrase phrase = phraseRepository.findById(id).orElse(null);

        if (phrase == null) {
            throw new RuntimeException("Phrase not found");
        }

        phrase.setEnglish(phraseRequestDTO.english());
        phrase.setTagalog(phraseRequestDTO.tagalog());

        phrase.setIsQuestion(phraseRequestDTO.isQuestion() != null && phraseRequestDTO.isQuestion());

        // Validates notation
        phraseValidator.validateWordIdLinkedMeaningConjugationOrder(phraseRequestDTO.phraseWordOrder());

        // If validation passed, convert to string to store into database
        String wordOrder = ServiceUtil.convertOrderArrayToString(phraseRequestDTO.phraseWordOrder());
        phrase.setPhraseWordOrder(wordOrder);

//        // Validates Word IDs
//        List<Word> words = wordRepository.findAllByIdIn(phraseDTO.wordIds());
//
//        // Some phrases will have blanks (ex: <person-name>, <origin-name>, <age>, etc.)
//        List<Long> wordIdsWithoutBlanks = new ArrayList<>();
//        for (Long wordId : phraseDTO.wordIds()) {
//            if (wordId != -1) wordIdsWithoutBlanks.add(wordId);
//        }
//
//        // Comparing found word ids with word ID array without blanks
//        if (words.size() != wordIdsWithoutBlanks.size()) {
//            throw new RuntimeException("Some Word IDs do not exists in " + phraseDTO.tagalog());
//        }
//
//        phrase.setWords(words);

        return phraseRepository.save(phrase);
    }

    public void deletePhraseById(Long id) {
        phraseRepository.deleteById(id);
    }
}
