package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.PhraseMapper;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.phrases.PhraseWord;
import org.alouastudios.easytagalogbackend.model.words.Conjugation;
import org.alouastudios.easytagalogbackend.model.words.English;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.repository.EnglishRepository;
import org.alouastudios.easytagalogbackend.repository.PhraseRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.alouastudios.easytagalogbackend.util.ServiceUtil;
import org.alouastudios.easytagalogbackend.validator.PhraseValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhraseService {

    private final PhraseRepository phraseRepository;
    private final WordRepository wordRepository;
    private final EnglishRepository englishRepository;

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

    // TODO: For OpenAI API
    @Transactional
    public void generateGrammarBreakdowns(@PathVariable Long uuid) {
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

                        String audioUrl = "";

                        // Use audio url of linked word
                        if (Boolean.TRUE.equals(pw.useLinkedWord()) && word.getLinkedWord() != null) {
                            audioUrl = word.getLinkedWord().getAudioUrl();

                        } else if (pw.tense() != null) {

                            // Use audio url of conjugated verb
                            Conjugation conjugation = word.getConjugations().stream()
                                    .filter(c -> c.getTense().equals(pw.tense()))
                                    .findFirst()
                                    .orElseThrow(() -> new ResourceNotFoundException("Tense not found"));
                            audioUrl = conjugation.getAudioUrl();
                        } else {

                            // Else use regular tagalog audio url
                            audioUrl = word.getAudioUrl();

                            // In case word does not have audio url
                            if (audioUrl == null || audioUrl.isBlank()) {
                                audioUrl = ServiceUtil.createWordAudioString(word.getTagalog());
                            }
                        }

                        phraseWord.setAudioUrl(audioUrl);
                        phraseWord.setWord(word);
                        phraseWord.setNote(pw.note());

                        English english = englishRepository.findByUuid(pw.englishUuid()).orElseThrow(() -> new ResourceNotFoundException("English not found"));
                        phraseWord.setEnglish(english.getMeaning());
                    }

                    phraseWord.setPhrase(phrase);
                    return phraseWord;
                })
                .sorted(Comparator.comparingInt(PhraseWord::getPosition))
                .collect(Collectors.toCollection(ArrayList::new));

        // Process and set phrase audio url
        if (phraseRequest.audioUrl() == null || phraseRequest.audioUrl().isEmpty()) {
            phrase.setAudioUrl(ServiceUtil.createPhraseAudioString(phraseRequest.tagalog()));
        }

        phraseMapper.toEntity(phrase, phraseRequest, phraseWords);
    }
}
