package org.alouastudios.easytagalogbackend.service;

import org.alouastudios.easytagalogbackend.dto.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.QuestionRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.lessonResponse.LessonResponseDTO;
import org.alouastudios.easytagalogbackend.dto.response.lessonResponse.QuestionResponseDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.LessonMapper;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.model.lessons.Question;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.repository.LessonRepository;
import org.alouastudios.easytagalogbackend.repository.PhraseRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.alouastudios.easytagalogbackend.validator.LessonValidator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final WordRepository wordRepository;
    private final PhraseRepository phraseRepository;

    private final LessonValidator lessonValidator;

    private final LessonMapper lessonMapper;

    public LessonService(
            LessonRepository lessonRepository,
            WordRepository wordRepository,
            PhraseRepository phraseRepository,
            LessonValidator lessonValidator,
            LessonMapper lessonMapper) {

        this.lessonRepository = lessonRepository;
        this.wordRepository = wordRepository;
        this.phraseRepository = phraseRepository;
        this.lessonValidator = lessonValidator;
        this.lessonMapper = lessonMapper;
    }

    public List<LessonResponseDTO> getAllLessons() {

        List<Lesson> foundLessons = lessonRepository.findAll();

        List<LessonResponseDTO> lessonResponseDTOs = new ArrayList<>();

        for (Lesson lesson : foundLessons) {
            lessonResponseDTOs.add(lessonMapper.toResponseDTO(lesson));
        }

        return lessonResponseDTOs;
    }

    public LessonResponseDTO getLessonByUUID(UUID uuid) {
        Lesson foundLesson = lessonRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        return lessonMapper.toResponseDTO(foundLesson);
    }

    public LessonResponseDTO addLesson(LessonRequestDTO lessonRequestDTO) {

        // First validate request data
        lessonValidator.validateLessonRequest(lessonRequestDTO);

        Lesson newLesson = new Lesson();

        // Go through each question
        Set<Question> newQuestions = new HashSet<>();
        for (QuestionRequestDTO questionRequest : lessonRequestDTO.questions()) {
            newQuestions.add(addQuestion(questionRequest));
        }


        return lessonMapper.toResponseDTO(newLesson);
    }

    // TODO 10/29: FINISH LOGIC FOR CHANGING QUESTION INTO ENTITIES

    // This function handles populating all the fields with a UUID
    private Question addQuestion(QuestionRequestDTO questionRequestDTO) {

        // NOTE: Questions were validated before, so need to do checks

        Question newQuestion = new Question();

        // Need to fetch the Word or Phrase

        // Need to fetch Word/Phrase options

        return(lessonMapper.toQuestionEntity(questionRequestDTO, newQuestion);
    }

    private void addTranslateWordQuestion(QuestionRequestDTO questionRequestDTO, Question question) {

    }
}
