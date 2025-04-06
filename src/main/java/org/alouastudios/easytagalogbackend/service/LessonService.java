package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.lesson.*;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.LessonMapper;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.model.lessons.LessonQuestion;
import org.alouastudios.easytagalogbackend.model.lessons.TranslatePhraseQuestion;
import org.alouastudios.easytagalogbackend.model.lessons.TranslateWordQuestion;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.repository.LessonRepository;
import org.alouastudios.easytagalogbackend.repository.PhraseRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
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

    public List<LessonResponseDTO> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(lessonMapper::toResponseDTO)
                .toList();
    }

    public LessonResponseDTO getLessonByUUID(UUID uuid) {
        Lesson foundLesson = lessonRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

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

    private void handleLessonChanges(Lesson lesson, LessonRequestDTO lessonRequestDTO) {

        List<LessonQuestion> questions = new ArrayList<>();

        for (LessonQuestionRequestDTO questionDTO : lessonRequestDTO.questions()) {
            if (questionDTO instanceof TranslateWordQuestionRequestDTO wordDTO) {
                questions.add(getTranslateWordQuestion(wordDTO, lesson));

            } else if (questionDTO instanceof TranslatePhraseQuestionRequestDTO phraseDTO) {
                questions.add(getTranslatePhraseQuestion(phraseDTO, lesson));

            } else {
                throw new IllegalArgumentException("Unsupported question type: " + questionDTO.getClass().getSimpleName());
            }
        }

        lesson.setQuestions(questions);
    }

    // This function returns a TranslateWordQuestion
    private TranslateWordQuestion getTranslateWordQuestion(TranslateWordQuestionRequestDTO translateWordQuestionRequestDTO, Lesson lesson) {
        TranslateWordQuestion question = new TranslateWordQuestion();

        question.setPrompt(translateWordQuestionRequestDTO.getPrompt());
        question.setLesson(lesson);
        question.setAnswer(translateWordQuestionRequestDTO.getAnswer());

        // Fetch and set word options
        List<Word> options = translateWordQuestionRequestDTO.getOptions().stream()
                .map(uuid -> wordRepository.findByUuid(uuid)
                        .orElseThrow(() -> new ResourceNotFoundException("Word option not found: " + uuid)))
                .toList();
        question.setOptions(options);

        return question;
    }

    // This function returns a TranslatePhraseQuestion
    private TranslatePhraseQuestion getTranslatePhraseQuestion(TranslatePhraseQuestionRequestDTO translatePhraseQuestionRequestDTO, Lesson lesson) {
        TranslatePhraseQuestion question = new TranslatePhraseQuestion();

        question.setPrompt(translatePhraseQuestionRequestDTO.getPrompt());
        question.setLesson(lesson);
        question.setAnswer(translatePhraseQuestionRequestDTO.getAnswer());

        // Fetch and set phrase options
        List<Phrase> options = translatePhraseQuestionRequestDTO.getOptions().stream()
                .map(uuid -> phraseRepository.findByUuid(uuid)
                        .orElseThrow(() -> new ResourceNotFoundException("Phrase option not found: " + uuid)))
                .toList();
        question.setOptions(options);

        return question;
    }
}
