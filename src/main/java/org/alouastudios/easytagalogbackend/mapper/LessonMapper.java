package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.lesson.*;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.dto.word.WordResponseDTO;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.model.lessons.LessonQuestion;
import org.alouastudios.easytagalogbackend.model.lessons.TranslatePhraseQuestion;
import org.alouastudios.easytagalogbackend.model.lessons.TranslateWordQuestion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LessonMapper {

    private final WordMapper wordMapper;
    private final PhraseMapper phraseMapper;

    public LessonMapper(WordMapper wordMapper, PhraseMapper phraseMapper) {
        this.wordMapper = wordMapper;
        this.phraseMapper = phraseMapper;
    }

    public LessonResponseDTO toResponseDTO(Lesson lesson) {
        return new LessonResponseDTO(
                lesson.getUuid(),
                lesson.getTitle(),
                lesson.getQuestions().stream().map(this::toLessonQuestionResponseDTO).collect(Collectors.toSet())
        );
    }

    private LessonQuestionResponseDTO toLessonQuestionResponseDTO(LessonQuestion lessonQuestion) {
        if (lessonQuestion instanceof TranslateWordQuestion translateWordQuestion) {
            List<WordResponseDTO> options = translateWordQuestion.getOptions()
                    .stream()
                    .map(wordMapper::toResponseDTO)
                    .toList();

            return new TranslateWordQuestionResponseDTO(
                    translateWordQuestion.getPrompt(),
                    options,
                    translateWordQuestion.getAnswer()
            );

        } else if (lessonQuestion instanceof TranslatePhraseQuestion translatePhraseQuestion) {
            List<PhraseResponseDTO> options = translatePhraseQuestion.getOptions()
                    .stream()
                    .map(phraseMapper::toResponseDTO)
                    .toList();

            return new TranslatePhraseQuestionResponseDTO(
                    translatePhraseQuestion.getPrompt(),
                    options,
                    translatePhraseQuestion.getAnswer()
            );

        } else {
            throw new IllegalArgumentException("Unknown question type: " + lessonQuestion.getClass());
        }
    }
}
