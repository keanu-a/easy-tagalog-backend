package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.lesson.*;
import org.alouastudios.easytagalogbackend.dto.phrase.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.dto.word.WordResponseDTO;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.model.lessons.LessonItem;
import org.alouastudios.easytagalogbackend.model.lessons.TranslatePhraseItem;
import org.alouastudios.easytagalogbackend.model.lessons.TranslateWordItem;
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
                lesson.getItems().stream().map(this::toLessonQuestionResponseDTO).collect(Collectors.toList())
        );
    }

    public void toEntity(Lesson lesson, LessonRequestDTO lessonRequestDTO, List<LessonItem> lessonItems) {
        lesson.setTitle(lessonRequestDTO.title());

        lesson.getItems().clear();
        lesson.getItems().addAll(lessonItems);
    }

    private LessonItemResponseDTO toLessonQuestionResponseDTO(LessonItem lessonItem) {
        if (lessonItem instanceof TranslateWordItem translateWordQuestion) {
            List<WordResponseDTO> options = translateWordQuestion.getOptions()
                    .stream()
                    .map(word -> wordMapper.toResponseDTO(word, List.of()))
                    .toList();

            return new TranslateWordItemResponseDTO(
                    options,
                    translateWordQuestion.getAnswer()
            );

        } else if (lessonItem instanceof TranslatePhraseItem translatePhraseQuestion) {
            List<PhraseResponseDTO> options = translatePhraseQuestion.getOptions()
                    .stream()
                    .map(phraseMapper::toResponseDTO)
                    .toList();

            return new TranslatePhraseItemResponseDTO(
                    options,
                    translatePhraseQuestion.getAnswer()
            );

        } else {
            throw new IllegalArgumentException("Unknown question type: " + lessonItem.getClass());
        }
    }
}
