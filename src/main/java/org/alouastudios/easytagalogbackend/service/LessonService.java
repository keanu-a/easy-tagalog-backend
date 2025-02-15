//package org.alouastudios.easytagalogbackend.service;
//
//import jakarta.transaction.Transactional;
//import org.alouastudios.easytagalogbackend.dto.lesson.LessonRequestDTO;
//import org.alouastudios.easytagalogbackend.dto.lesson.QuestionRequestDTO;
//import org.alouastudios.easytagalogbackend.dto.lesson.LessonResponseDTO;
//import org.alouastudios.easytagalogbackend.enums.QuestionType;
//import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
//import org.alouastudios.easytagalogbackend.mapper.LessonMapper;
//import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
//import org.alouastudios.easytagalogbackend.model.lessons.Question;
//import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
//import org.alouastudios.easytagalogbackend.model.words.English;
//import org.alouastudios.easytagalogbackend.model.words.Word;
//import org.alouastudios.easytagalogbackend.repository.LessonRepository;
//import org.alouastudios.easytagalogbackend.repository.PhraseRepository;
//import org.alouastudios.easytagalogbackend.repository.QuestionRepository;
//import org.alouastudios.easytagalogbackend.repository.WordRepository;
//import org.alouastudios.easytagalogbackend.validator.LessonValidator;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class LessonService {
//
//    private final LessonRepository lessonRepository;
//    private final WordRepository wordRepository;
//    private final PhraseRepository phraseRepository;
//    private final QuestionRepository questionRepository;
//
//    private final LessonValidator lessonValidator;
//
//    private final LessonMapper lessonMapper;
//
//    public LessonService(
//            LessonRepository lessonRepository,
//            WordRepository wordRepository,
//            PhraseRepository phraseRepository, QuestionRepository questionRepository,
//            LessonValidator lessonValidator,
//            LessonMapper lessonMapper) {
//
//        this.lessonRepository = lessonRepository;
//        this.wordRepository = wordRepository;
//        this.phraseRepository = phraseRepository;
//        this.questionRepository = questionRepository;
//
//        this.lessonValidator = lessonValidator;
//
//        this.lessonMapper = lessonMapper;
//    }
//
//    public List<LessonResponseDTO> getAllLessons() {
//
//        List<Lesson> foundLessons = lessonRepository.findAll();
//
//        List<LessonResponseDTO> lessonResponseDTOs = new ArrayList<>();
//
//        for (Lesson lesson : foundLessons) {
//            lessonResponseDTOs.add(lessonMapper.toResponseDTO(lesson));
//        }
//
//        return lessonResponseDTOs;
//    }
//
//    public LessonResponseDTO getLessonByUUID(UUID uuid) {
//        Lesson foundLesson = lessonRepository
//                .findByUuid(uuid)
//                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
//
//        return lessonMapper.toResponseDTO(foundLesson);
//    }
//
//    @Transactional
//    public LessonResponseDTO addLesson(LessonRequestDTO lessonRequestDTO) {
//
//        // First validate request data
//        lessonValidator.validateLessonRequest(lessonRequestDTO);
//
//        Lesson newLesson = new Lesson();
//
//        // Go through each question and saves to the database
//        Set<Question> newQuestions = new HashSet<>();
//        for (QuestionRequestDTO questionRequest : lessonRequestDTO.questions()) {
//            Question newQuestion = addQuestion(questionRequest);
//            newQuestions.add(newQuestion);
//            questionRepository.save(newQuestion);
//        }
//
//        lessonMapper.toLessonEntity(lessonRequestDTO, newLesson, newQuestions);
//        lessonRepository.save(newLesson);
//
//        return lessonMapper.toResponseDTO(newLesson);
//    }
//
//    public void deleteLessonByUuid(UUID uuid) {
//        Lesson foundLesson = lessonRepository.findByUuid(uuid).orElseThrow(
//                () -> new ResourceNotFoundException("Lesson not found")
//        );
//
//        lessonRepository.delete(foundLesson);
//    }
//
//    // This function handles populating all the fields with a UUID
//    private Question addQuestion(QuestionRequestDTO questionRequestDTO) {
//
//        // NOTE: Questions were validated before, so there is no need to do checks
//
//        Question newQuestion = new Question();
//
//        switch (questionRequestDTO.questionType()) {
//
//            case QuestionType.TRANSLATE_WORD:
//                addTranslateWordQuestion(questionRequestDTO, newQuestion);
//                break;
//
//            case QuestionType.TRANSLATE_PHRASE:
//                addTranslatePhraseQuestion(questionRequestDTO, newQuestion);
//                break;
//
//        }
//
//        return newQuestion;
//    }
//
//    // This function handles getting all Word data for "TRANSLATE_WORD" questions
//    private void addTranslateWordQuestion(QuestionRequestDTO questionRequestDTO, Question newQuestion) {
//        Word questionWord = wordRepository
//                .findByUuid(questionRequestDTO.wordId())
//                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));
//
//        // Converting the set into a list to be used by the repository
//        List<UUID> wordUuidList = new ArrayList<>(questionRequestDTO.wordOptions());
//        List<Word> questionWordOptionsList = wordRepository.findAllByUuidIn(wordUuidList);
//
//        // Checks that all word UUIDs given were found
//        if (questionWordOptionsList.isEmpty() ||
//                questionWordOptionsList.size() != questionRequestDTO.wordOptions().size()) {
//            throw new ResourceNotFoundException("Not all word options were found");
//        }
//
//        Set<Word> questionWordOptions = new HashSet<>(questionWordOptionsList);
//
//        lessonMapper.toQuestionEntity(questionRequestDTO, newQuestion, questionWord, questionWordOptions);
//    }
//
//    // This function handles getting all Phrase data for "TRANSLATE_PHRASE" questions
//    private void addTranslatePhraseQuestion(QuestionRequestDTO questionRequestDTO, Question newQuestion) {
//        Phrase questionPhrase = phraseRepository
//                .findByUuid(questionRequestDTO.phraseId())
//                .orElseThrow(() -> new ResourceNotFoundException("Phrase not found"));
//
//        // Converting the set into a list to be used by the repository
//        List<UUID> phraseUuidList = new ArrayList<>(questionRequestDTO.phraseOptions());
//        List<Phrase> questionPhraseOptionsList = phraseRepository.findAllByUuidIn(phraseUuidList);
//
//        // Checks that all phrase UUIDs given were found
//        if (questionPhraseOptionsList.isEmpty() ||
//                questionPhraseOptionsList.size() != questionRequestDTO.phraseOptions().size()) {
//            throw new ResourceNotFoundException("Not all phrase options were found");
//        }
//
//        Set<Phrase> questionPhraseOptions = new HashSet<>(questionPhraseOptionsList);
//
//        lessonMapper.toQuestionEntity(questionRequestDTO, newQuestion, questionPhrase, questionPhraseOptions);
//    }
//}
