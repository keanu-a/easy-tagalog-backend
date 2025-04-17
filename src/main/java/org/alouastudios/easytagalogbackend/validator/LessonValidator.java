package org.alouastudios.easytagalogbackend.validator;

import jakarta.validation.ValidationException;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonQuestionRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.TranslatePhraseQuestionRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.TranslateWordQuestionRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class LessonValidator {

    public void validateLessonRequest(LessonRequestDTO lessonRequest) {
        if (lessonRequest.title() == null || lessonRequest.title().isBlank()) {
            throw new ValidationException("Lessons must have a title");
        }

        if (lessonRequest.questions() == null || lessonRequest.questions().isEmpty()) {
            throw new ValidationException("Lesson must have at least one question");
        }

        for (LessonQuestionRequestDTO question: lessonRequest.questions()) {

            if (question instanceof TranslateWordQuestionRequestDTO translateWordQuestion) {
                validateTranslateWordQuestion(translateWordQuestion);

            } else if (question instanceof TranslatePhraseQuestionRequestDTO translatePhraseQuestion) {
                validateTranslatePhraseQuestion(translatePhraseQuestion);

            } else {
                throw new ValidationException("Question is of invalid type");
            }
        }
    }

    private void validateTranslateWordQuestion(TranslateWordQuestionRequestDTO translateWordQuestion) {
        if (translateWordQuestion.getOptions() == null || translateWordQuestion.getOptions().isEmpty()) {
            throw new ValidationException("Translate word question must have at least one option");
        }

        if (translateWordQuestion.getOptions().size() < 2) {
            throw new ValidationException("Translate word question must have at least two options");
        }

        if (translateWordQuestion.getAnswer() == null) {
            throw new ValidationException("Translate word question must have an answer");
        }
    }

    private void validateTranslatePhraseQuestion(TranslatePhraseQuestionRequestDTO translatePhraseQuestion) {
        if (translatePhraseQuestion.getOptions() == null || translatePhraseQuestion.getOptions().isEmpty()) {
            throw new ValidationException("Translate phrase question must have at least one option");
        }

        if (translatePhraseQuestion.getOptions().size() < 2) {
            throw new ValidationException("Translate phrase question must have at least two options");
        }

        if (translatePhraseQuestion.getAnswer() == null) {
            throw new ValidationException("Translate phrase question must have an answer");
        }
    }
}
