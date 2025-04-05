package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonResponseDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.LessonMapper;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.model.lessons.LessonQuestion;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
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

        // handleLessonChanges(newLesson, lessonRequest);

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

        // handleLessonChanges(foundLesson, lessonRequest);

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
    }
}
