package org.alouastudios.easytagalogbackend.controller;

import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public List<Lesson> getAllLessons() { return lessonService.getAllLessons(); }

    @GetMapping("/{id}")
    public Lesson getLessonById(@PathVariable long id) { return lessonService.getLessonById(id); }
}
