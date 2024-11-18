package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.PhraseMapper;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.phrases.PhraseWordGuide;
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

    public List<PhraseResponseDTO> getAllPhrases() {

        List<Phrase> foundPhrases = phraseRepository.findAll();
        List<PhraseResponseDTO> phrases = new ArrayList<>();

        for (Phrase phrase : foundPhrases) {
            phrases.add(phraseMapper.toResponseDTO(phrase));
        }

        return phrases;
    }

    public PhraseResponseDTO getPhraseById(UUID uuid) {

        Phrase foundPhrase = phraseRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));

        return phraseMapper.toResponseDTO(foundPhrase);
    }

    public PhraseResponseDTO addPhrase(PhraseRequestDTO phraseRequest) {

        // First validate the request
        phraseValidator.validatePhraseRequest(phraseRequest);

        // Gets each Word object from the database needed for the phrase
        Set<Word> phraseWords = getExistingWords(phraseRequest.wordUuids());

        // Creating string list for the Phrase entity in the database
        List<String> englishMeanings = getEnglishMeanings(phraseRequest.phraseWordGuides(), phraseWords);

        Phrase newPhrase = new Phrase();
        phraseMapper.toEntity(newPhrase, phraseRequest, phraseWords, englishMeanings);
        phraseRepository.save(newPhrase);

        // Map the new phrase entity into PhraseResponseDTO
        return phraseMapper.toResponseDTO(newPhrase);
    }

    @Transactional
    public List<PhraseResponseDTO> addPhrases(List<PhraseRequestDTO> phrases) {

        List<PhraseResponseDTO> newPhrases = new ArrayList<>();

        for (PhraseRequestDTO phraseRequestDTO : phrases) {
            newPhrases.add(addPhrase(phraseRequestDTO));
        }

        return newPhrases;
    }

    public PhraseResponseDTO updatePhrase(UUID uuid, PhraseRequestDTO phraseRequest) {

        phraseValidator.validatePhraseRequest(phraseRequest);

        Phrase foundPhrase = phraseRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));

        Set<Word> foundWords = getExistingWords(phraseRequest.wordUuids());
        List<String> foundEnglishMeanings = getEnglishMeanings(phraseRequest.phraseWordGuides(), foundWords);

        phraseMapper.toEntity(foundPhrase, phraseRequest, foundWords, foundEnglishMeanings);
        phraseRepository.save(foundPhrase);

        return phraseMapper.toResponseDTO(foundPhrase);
    }

    public void deletePhraseById(UUID uuid) {
        Phrase foundPhrase = phraseRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));

        phraseRepository.delete(foundPhrase);
    }

    // This function takes in a set of UUIDs from a phrase request object, and returns the set of word entities
    private Set<Word> getExistingWords(Set<UUID> phraseWordUUIDs) {

        Set<Word> phraseWords = new HashSet<>();
        for (UUID uuid : phraseWordUUIDs) {
            Word phraseWord = wordRepository
                    .findByUuid(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Word with id '" + uuid + "' not found"));

            phraseWords.add(phraseWord);
        }

        return phraseWords;
    }

    // This function gets all english meanings based off the request phrase word guides
    private List<String> getEnglishMeanings(List<PhraseWordGuide> phraseWordGuides, Set<Word> phraseWords) {

        // Turned phraseWords into a map for finding needed english meaning
        HashMap<UUID, Word> phraseWordMap = new HashMap<>();
        for (Word word : phraseWords) {
            phraseWordMap.put(word.getUuid(), word);
        }

        // Creating string list for the Phrase entity in the database
        List<String> englishMeanings = new ArrayList<>();

        for (PhraseWordGuide wordGuide : phraseWordGuides) {

            // First check if place/person name
            if (wordGuide.nameType() != null) {
                englishMeanings.add("<" + wordGuide.nameType().name() + ">");
                continue;
            }

            Word usedWord = phraseWordMap.get(wordGuide.uuid());

            // Adds english wordGuide of needed conjugation
            if (wordGuide.tense() != null) {
                Conjugation usedConjugation = usedWord.getConjugation(wordGuide.tense());
                englishMeanings.add(usedConjugation.getEnglish());
                continue;
            }

            // Else adds english wordGuide from the english list with matching uuid
            English usedEnglish = usedWord.getEnglish(wordGuide.englishUuid());
            englishMeanings.add(usedEnglish.getMeaning());
        }

        return englishMeanings;
    }
}
