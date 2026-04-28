package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.audio.AudioDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.*;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.LessonMapper;
import org.alouastudios.easytagalogbackend.model.lessons.*;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.words.English;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.repository.*;
import org.alouastudios.easytagalogbackend.validator.LessonValidator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final WordRepository wordRepository;
    private final PhraseRepository phraseRepository;

    private final LessonValidator lessonValidator;

    private final LessonMapper lessonMapper;
    private final EnglishRepository englishRepository;
    private final LessonItemRepository lessonItemRepository;

    private final S3SignedUrlService s3SignedUrlService;

    public List<LessonResponseDTO> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(lessonMapper::toResponseDTO)
                .toList();
    }

    public List<LessonSummaryDTO> getLessonSummaries() {
        return lessonRepository.findAll()
                .stream()
                .map(lesson -> new LessonSummaryDTO(lesson.getUuid(), lesson.getTitle()))
                .toList();
    }

    public LessonResponseDTO getLessonByUUID(UUID uuid) {
        Lesson foundLesson = lessonRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        foundLesson.getItems().forEach(item -> {
            if (item instanceof ScenarioPromptItem scenarioPromptItem) {
                scenarioPromptItem.getPromptPhrase().setAudioUrl(
                        s3SignedUrlService.generateSignedUrl(scenarioPromptItem.getPromptPhrase().getAudioUrl()));
                scenarioPromptItem.getOptions().forEach(option -> option.setAudioUrl(
                        s3SignedUrlService.generateSignedUrl(option.getAudioUrl())
                ));
            } else if (item instanceof TranslateWordItem translateWordItem) {
                translateWordItem.getOptions().forEach(option -> option.setAudioUrl(
                        s3SignedUrlService.generateSignedUrl(option.getAudioUrl())
                ));
            } else if (item instanceof TranslatePhraseItem translatePhraseItem) {
                translatePhraseItem.getOptions().forEach(option -> option.setAudioUrl(
                        s3SignedUrlService.generateSignedUrl(option.getAudioUrl())
                ));
            }
        });

        return lessonMapper.toResponseDTO(foundLesson);
    }

    @Transactional
    public LessonResponseDTO addLesson(LessonRequestDTO lessonRequest) {
        Lesson newLesson = new Lesson();

        handleLessonChanges(newLesson, lessonRequest);

        lessonRepository.save(newLesson);

        return lessonMapper.toResponseDTO(newLesson);
    }

    @Transactional
    public List<LessonResponseDTO> addLessons(List<LessonRequestDTO> lessons) {
        List<LessonResponseDTO> newLessons = new ArrayList<>();

        for (LessonRequestDTO lessonRequestDTO : lessons) {
            newLessons.add(addLesson(lessonRequestDTO));
        }

        return newLessons;
    }

    public LessonResponseDTO updateLesson(UUID uuid, LessonRequestDTO lessonRequest) {
        Lesson foundLesson = lessonRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        handleLessonChanges(foundLesson, lessonRequest);

        lessonRepository.save(foundLesson);

        return lessonMapper.toResponseDTO(foundLesson);
    }

    public void deleteLessonById(UUID uuid) {
        Lesson foundLesson = lessonRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        lessonRepository.delete(foundLesson);
    }

    public boolean checkAnswer(UUID lessonItemUuid, UUID selectedOptionUuid) {
        LessonItem lessonItem = lessonItemRepository.findLessonItemByUuid(lessonItemUuid).orElseThrow(() -> new ResourceNotFoundException("LessonItem not found"));

        if (lessonItem instanceof TranslateWordItem translateWordItem) {
            return translateWordItem.getAnswer() == selectedOptionUuid;
        } else if (lessonItem instanceof TranslatePhraseItem translatePhraseItem) {
            return translatePhraseItem.getAnswer() == selectedOptionUuid;
        } else {
            throw new UnsupportedOperationException("Unsupported item type for answer checking");
        }
    }

    private void handleLessonChanges(Lesson lesson, LessonRequestDTO lessonRequestDTO) {

        lessonValidator.validateLessonRequest(lessonRequestDTO);

        List<LessonItem> lessonItems = new ArrayList<>();

        for (LessonItemRequestDTO lessonItemRequestDto : lessonRequestDTO.items()) {
            switch (lessonItemRequestDto) {
                case TranslateWordItemRequestDTO translateWordItemRequestDTO ->
                        lessonItems.add(getTranslateWordItem(translateWordItemRequestDTO, lesson));
                case TranslatePhraseItemRequestDTO translatePhraseItemRequestDTO ->
                        lessonItems.add(getTranslatePhraseItem(translatePhraseItemRequestDTO, lesson));
                case ScenarioPromptItemRequestDTO scenarioPromptItemRequestDTO ->
                        lessonItems.add(getScenarioPromptItem(scenarioPromptItemRequestDTO, lesson));
                case null, default -> {
                    assert lessonItemRequestDto != null;
                    throw new IllegalArgumentException("Unsupported question type: " + lessonItemRequestDto.getClass().getSimpleName());
                }
            }
        }

        lessonMapper.toEntity(lesson, lessonRequestDTO, lessonItems);
    }

    // This function returns a TranslateWordItem
    private TranslateWordItem getTranslateWordItem(TranslateWordItemRequestDTO translateWordItemRequestDTO, Lesson lesson) {
        TranslateWordItem newTranslateWordItem = new TranslateWordItem();

        newTranslateWordItem.setLesson(lesson);
        newTranslateWordItem.setAnswer(translateWordItemRequestDTO.getWordAnswerUuid());

        English english = englishRepository.findByUuid(translateWordItemRequestDTO.getEnglishUuid()).orElseThrow(() -> new ResourceNotFoundException("English meaning not found"));
        newTranslateWordItem.setEnglish(english.getMeaning());

        // Fetch and set word options
        List<Word> options = translateWordItemRequestDTO.getWordOptionsUuid().stream()
                .map(uuid -> wordRepository.findByUuid(uuid)
                        .orElseThrow(() -> new ResourceNotFoundException("Word option not found: " + uuid)))
                .toList();
        newTranslateWordItem.setOptions(options);

        return newTranslateWordItem;
    }

    // This function returns a TranslatePhraseItem
    private TranslatePhraseItem getTranslatePhraseItem(TranslatePhraseItemRequestDTO translatePhraseItemRequestDTO, Lesson lesson) {
        TranslatePhraseItem newTranslatePhraseItem = new TranslatePhraseItem();

        newTranslatePhraseItem.setLesson(lesson);
        newTranslatePhraseItem.setAnswer(translatePhraseItemRequestDTO.getPhraseAnswerUuid());

        Phrase phrase = phraseRepository.findByUuid(translatePhraseItemRequestDTO.getPhraseUuid()).orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));
        newTranslatePhraseItem.setEnglish(phrase.getEnglish());

        // Fetch and set phrase options
        List<Phrase> options = translatePhraseItemRequestDTO.getPhraseOptionsUuid().stream()
                .map(uuid -> phraseRepository.findByUuid(uuid)
                        .orElseThrow(() -> new ResourceNotFoundException("Phrase option not found: " + uuid)))
                .toList();
        newTranslatePhraseItem.setOptions(options);

        return newTranslatePhraseItem;
    }

    // This function returns a ScenarioPromptItem
    private ScenarioPromptItem getScenarioPromptItem(ScenarioPromptItemRequestDTO scenarioPromptItemRequestDTO, Lesson lesson) {
        ScenarioPromptItem newScenarioPromptItem = new ScenarioPromptItem();

        newScenarioPromptItem.setLesson(lesson);

        Phrase phrase = phraseRepository.findByUuid(scenarioPromptItemRequestDTO.getPhraseUuid()).orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));
        newScenarioPromptItem.setPromptPhrase(phrase);

        // Fetch and set phrase options
        List<Phrase> options = scenarioPromptItemRequestDTO.getOptions().stream()
                .map(uuid -> phraseRepository.findByUuid(uuid)
                        .orElseThrow(() -> new ResourceNotFoundException("Phrase option not found: " + uuid)))
                .toList();
        newScenarioPromptItem.setOptions(options);

        return newScenarioPromptItem;
    }
}
