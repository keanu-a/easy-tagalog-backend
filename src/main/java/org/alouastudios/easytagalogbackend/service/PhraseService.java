package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import org.alouastudios.easytagalogbackend.dto.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.enums.Tense;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.PhraseMapper;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.phrases.PhraseWordMeaning;
import org.alouastudios.easytagalogbackend.model.words.Conjugation;
import org.alouastudios.easytagalogbackend.model.words.English;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.repository.PhraseRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.alouastudios.easytagalogbackend.validator.PhraseValidator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PhraseService {

    private final PhraseRepository phraseRepository;

    private final WordRepository wordRepository;

    private final PhraseValidator phraseValidator;

    private final PhraseMapper phraseMapper;

    public PhraseService(
            PhraseRepository phraseRepository,
            WordRepository wordRepository,
            PhraseValidator phraseValidator,
            PhraseMapper phraseMapper
    ) {
        this.phraseRepository = phraseRepository;
        this.wordRepository = wordRepository;
        this.phraseValidator = phraseValidator;
        this.phraseMapper = phraseMapper;
    }

    public List<Phrase> getAllPhrases() {
        return phraseRepository.findAll();
    }

    public Phrase getPhraseById(Long id) {
        return phraseRepository.findById(id).orElse(null);
    }

    public Phrase addPhrase(PhraseRequestDTO phrase) {

        // TODO 10/23: Fix PhraseDTOs logic

        // There should be a meaning for each word of the phrase (even names)
        if (phrase.tagalog().split(" ").length != phrase.phraseWordMeanings().size()) {
            throw new RuntimeException("Phrase word order length doesn't match tagalog word length");
        }

        // Gets each Word object from the database needed for the phrase
        Set<Word> phraseWords = fetchPhraseWords(phrase.wordIds());

        // Turned phraseWords into a map for finding needed english meaning
        HashMap<UUID, Word> phraseWordMap = new HashMap<>();
        for (Word word : phraseWords) {
            phraseWordMap.put(word.getUuid(), word);
        }

        // Creating string list for the Phrase entity in the database
        List<String> entityPhraseWordMeanings = new ArrayList<>();
        for (PhraseWordMeaning meaning : phrase.phraseWordMeanings()) {

            if (meaning.nonDatabaseWord()) {
                entityPhraseWordMeanings.add("<person-name>");
                continue;
            }

            Word usedWord = phraseWordMap.get(meaning.id());

            if (meaning.tense() != null) {
                for (Conjugation conjugation : usedWord.getConjugations()) {
                    if (meaning.tense() == conjugation.getTense()) {
                        entityPhraseWordMeanings.add(conjugation.getEnglish());
                        break;
                    }
                }
                continue;
            }

            for (English english : usedWord.getEnglish()) {
                if (meaning.englishId() == english.getUuid()) {
                    entityPhraseWordMeanings.add(english.getMeaning());
                    break;
                }
            }

        }

        // Changes DTO to entity so can be saved to the database
        Phrase newPhrase = phraseMapper.toEntity(phrase, phraseWords);

        // Map the new phrase entity into PhraseResponseDTO

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

//        // Validates notation
//        phraseValidator.validatePhraseWordOrder(phraseRequestDTO.phraseWordOrder());
//
//        // If validation passed, convert to string to store into database
//        String wordOrder = ServiceUtil.convertOrderArrayToString(phraseRequestDTO.phraseWordOrder());
//        phrase.setPhraseWordOrder(wordOrder);

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

    // This function takes in a set of UUIDs from a phrase request object, and returns the set of word entities
    private Set<Word> fetchPhraseWords(Set<UUID> phraseWordUUIDs) {

        Set<Word> phraseWords = new HashSet<>();
        for (UUID uuid : phraseWordUUIDs) {
            Word phraseWord = wordRepository
                    .findByUuid(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Word with id '" + uuid + "' not found"));

            phraseWords.add(phraseWord);
        }

        return phraseWords;
    }
}
