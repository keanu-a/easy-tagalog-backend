package org.alouastudios.easytagalogbackend.controller;

import org.alouastudios.easytagalogbackend.dto.lesson.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonResponseDTO;
import org.alouastudios.easytagalogbackend.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public List<LessonResponseDTO> getAllLessons() { return lessonService.getAllLessons(); }

    @GetMapping("/{uuid}")
    public LessonResponseDTO getLessonById(@PathVariable UUID uuid) { return lessonService.getLessonByUUID(uuid); }

    @PostMapping
    public LessonResponseDTO addLesson(@RequestBody LessonRequestDTO lessonRequestDTO) {
        return lessonService.addLesson(lessonRequestDTO);
    }

    @PostMapping("/batch")
    public List<LessonResponseDTO> addLessonBatch(@RequestBody List<LessonRequestDTO> lessonRequests) {
        return lessonService.addLessons(lessonRequests);
    }

    @PutMapping("/{uuid}")
    public LessonResponseDTO updateLesson(@PathVariable UUID uuid, @RequestBody LessonRequestDTO lessonRequest) {
        return lessonService.updateLesson(uuid, lessonRequest);
    }

    @DeleteMapping("/{uuid}")
    public String deleteLesson(@PathVariable UUID uuid) {
        lessonService.deleteLessonById(uuid);
        return "Deleted Phrase UUID: " + uuid;
    }
}
