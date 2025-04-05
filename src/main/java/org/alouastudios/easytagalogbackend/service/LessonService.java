package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonResponseDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
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

    public List<LessonResponseDTO> getAllLessons() {
    }

    public LessonResponseDTO getLessonByUUID(UUID uuid) {
    }

    @Transactional
    public LessonResponseDTO addLesson(LessonRequestDTO lessonRequestDTO) {
    }

    @Transactional
    public List<LessonResponseDTO> addLessons(List<LessonRequestDTO> lessonRequestDTOList) {
    }

    public PhraseResponseDTO updatePhrase(PhraseRequestDTO phraseRequestDTO) {}

    public void deleteLessonById(UUID uuid) {
    }

    private void handleLessonChanges(Lesson lesson, LessonRequestDTO lessonRequestDTO) {}
}
