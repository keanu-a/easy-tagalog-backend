package org.alouastudios.easytagalogbackend.validator;

import jakarta.validation.ValidationException;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonItemRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.TranslatePhraseItemRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.TranslateWordItemRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class LessonValidator {

    public void validateLessonRequest(LessonRequestDTO lessonRequest) {
        if (lessonRequest.title() == null || lessonRequest.title().isBlank()) {
            throw new ValidationException("Lessons must have a title");
        }

        if (lessonRequest.items() == null || lessonRequest.items().isEmpty()) {
            throw new ValidationException("Lesson must have at least one question");
        }

        for (LessonItemRequestDTO question: lessonRequest.items()) {

            if (question instanceof TranslateWordItemRequestDTO translateWordQuestion) {
                validateTranslateWordQuestion(translateWordQuestion);

            } else if (question instanceof TranslatePhraseItemRequestDTO translatePhraseQuestion) {
                validateTranslatePhraseQuestion(translatePhraseQuestion);

            } else {
                throw new ValidationException("Question is of invalid type");
            }
        }
    }

    private void validateTranslateWordQuestion(TranslateWordItemRequestDTO translateWordQuestion) {
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

    private void validateTranslatePhraseQuestion(TranslatePhraseItemRequestDTO translatePhraseQuestion) {
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
