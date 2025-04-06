package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhraseService {

    private final PhraseRepository phraseRepository;
    private final WordRepository wordRepository;

    private final PhraseValidator phraseValidator;

    private final PhraseMapper phraseMapper;

    public List<PhraseResponseDTO> getAllPhrases() {
        return phraseRepository.findAll()
                .stream()
                .map(phraseMapper::toResponseDTO)
                .toList();
    }

    public PhraseResponseDTO getPhraseByUUID(UUID uuid) {
        Phrase foundPhrase = phraseRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));

        return phraseMapper.toResponseDTO(foundPhrase);
    }

    @Transactional
    public PhraseResponseDTO addPhrase(PhraseRequestDTO phraseRequest) {
        // Create Phrase entity
        Phrase newPhrase = new Phrase();

        handlePhraseChanges(newPhrase, phraseRequest);

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
        // Ensure phrase exists in the database
        Phrase foundPhrase = phraseRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));

        handlePhraseChanges(foundPhrase, phraseRequest);

        phraseRepository.save(foundPhrase);

        return phraseMapper.toResponseDTO(foundPhrase);
    }

    public void deletePhraseById(UUID uuid) {
        Phrase foundPhrase = phraseRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));

        phraseRepository.delete(foundPhrase);
    }

    private void handlePhraseChanges(Phrase phrase, PhraseRequestDTO phraseRequest) {

        phraseValidator.validatePhraseRequest(phraseRequest);

        List<PhraseWord> phraseWords = phraseRequest.phraseWords().stream()
                .map(pw -> {
                    PhraseWord phraseWord = new PhraseWord();

                    phraseWord.setPosition(pw.position());
                    phraseWord.setIsProperNoun(Boolean.TRUE.equals(pw.isProperNoun()));

                    if (!Boolean.TRUE.equals(pw.isProperNoun())) {
                        Word word = wordRepository.findByUuid(pw.wordUuid())
                                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));
                        phraseWord.setWord(word);
                        phraseWord.setEnglish(pw.english());
                        phraseWord.setNote(pw.note());
                    }

                    phraseWord.setPhrase(phrase);
                    return phraseWord;
                })
                .sorted(Comparator.comparingInt(PhraseWord::getPosition))
                .collect(Collectors.toCollection(ArrayList::new));

        phraseMapper.toEntity(phrase, phraseRequest, phraseWords);
    }
}
