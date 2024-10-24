package org.alouastudios.easytagalogbackend.service;

import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> getAllLessons() { return lessonRepository.findAll(); }

    public Lesson getLessonById(Long id) { return lessonRepository.findById(id).orElse(null); }
}
