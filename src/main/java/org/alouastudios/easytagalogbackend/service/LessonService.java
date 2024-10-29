package org.alouastudios.easytagalogbackend.service;

import org.alouastudios.easytagalogbackend.dto.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.lessonResponse.LessonResponseDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.LessonMapper;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.repository.LessonRepository;
import org.alouastudios.easytagalogbackend.repository.PhraseRepository;
import org.alouastudios.easytagalogbackend.repository.WordRepository;
import org.alouastudios.easytagalogbackend.validator.LessonValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        return lessonMapper.toResponseDTO(newLesson);
    }
}
