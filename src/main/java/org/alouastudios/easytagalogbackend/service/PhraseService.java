package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.PhraseMapper;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.phrases.PhraseWord;
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

        // Create Phrase entity
        Phrase newPhrase = new Phrase();
        newPhrase.setTagalog(phraseRequest.tagalog());
        newPhrase.setEnglish(phraseRequest.english());
        newPhrase.setIsQuestion(phraseRequest.isQuestion());

        // Convert PhraseWordRequestDTOs to PhraseWord entities
        List<PhraseWord> phraseWords = phraseRequest.phraseWords().stream()
                .map(pw -> {
                    PhraseWord phraseWord = new PhraseWord();

                    phraseWord.setPosition(pw.position());
                    phraseWord.setIsProperNoun(Boolean.TRUE.equals(pw.isProperNoun()));

                    if (!Boolean.TRUE.equals(pw.isProperNoun())) {
                        Word word = wordRepository.findByUuid(pw.wordUuid())
                                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));
                        phraseWord.setWord(word);
                        phraseWord.setEnglishMeaning(pw.englishMeaning());
                        phraseWord.setNote(pw.note());
                    }

                    phraseWord.setPhrase(newPhrase);
                    return phraseWord;
                })
                .sorted(Comparator.comparingInt(PhraseWord::getPosition))
                .toList();

        newPhrase.setPhraseWords(phraseWords);
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


        foundPhrase.setTagalog(phraseRequest.tagalog());
        foundPhrase.setEnglish(phraseRequest.english());
        foundPhrase.setIsQuestion(phraseRequest.isQuestion());

        foundPhrase.getPhraseWords().clear();

        List<PhraseWord> phraseWords = phraseRequest.phraseWords().stream()
                .map(pw -> {
                    PhraseWord phraseWord = new PhraseWord();

                    phraseWord.setPosition(pw.position());
                    phraseWord.setIsProperNoun(Boolean.TRUE.equals(pw.isProperNoun()));

                    if (!Boolean.TRUE.equals(pw.isProperNoun())) {
                        Word word = wordRepository.findByUuid(pw.wordUuid())
                                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));
                        phraseWord.setWord(word);
                        phraseWord.setEnglishMeaning(pw.englishMeaning());
                        phraseWord.setNote(pw.note());
                    }

                    phraseWord.setPhrase(foundPhrase);
                    return phraseWord;
                })
                .sorted(Comparator.comparingInt(PhraseWord::getPosition))
                .toList();

        foundPhrase.setPhraseWords(phraseWords);
        phraseRepository.save(foundPhrase);

        return phraseMapper.toResponseDTO(foundPhrase);
    }

    public void deletePhraseById(UUID uuid) {
        Phrase foundPhrase = phraseRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));

        phraseRepository.delete(foundPhrase);
    }
}
