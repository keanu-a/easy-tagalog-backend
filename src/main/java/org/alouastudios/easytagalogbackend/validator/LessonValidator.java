package org.alouastudios.easytagalogbackend.validator;

import jakarta.validation.ValidationException;
import org.alouastudios.easytagalogbackend.dto.lesson.*;
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

        for (LessonItemRequestDTO lessonItem: lessonRequest.items()) {
            switch (lessonItem) {
                case TranslateWordItemRequestDTO translateWordItemRequest ->
                        validateTranslateWordQuestion(translateWordItemRequest);
                case TranslatePhraseItemRequestDTO translatePhraseItemRequest ->
                        validateTranslatePhraseQuestion(translatePhraseItemRequest);
                case ScenarioPromptItemRequestDTO scenarioPromptItemRequest ->
                        validateScenarioPrompt(scenarioPromptItemRequest);
                case null, default -> throw new ValidationException("Question is of invalid type");
            }
        }
    }

    private void validateTranslateWordQuestion(TranslateWordItemRequestDTO translateWordItemRequest) {
        if (translateWordItemRequest.getEnglishUuid() == null) {
            throw new ValidationException("Translate word question must have a word");
        }

        if (translateWordItemRequest.getWordOptionsUuid() == null || translateWordItemRequest.getWordOptionsUuid().isEmpty()) {
            throw new ValidationException("Translate word question must have at least one option");
        }

        if (translateWordItemRequest.getWordOptionsUuid().size() < 2) {
            throw new ValidationException("Translate word question must have at least two options");
        }

        if (translateWordItemRequest.getWordAnswerUuid() == null) {
            throw new ValidationException("Translate word question must have an answer");
        }
    }

    private void validateTranslatePhraseQuestion(TranslatePhraseItemRequestDTO translatePhraseItemRequest) {
        if (translatePhraseItemRequest.getPhraseUuid() == null) {
            throw new ValidationException("Translate phrase question must have a word");
        }

        if (translatePhraseItemRequest.getPhraseOptionsUuid() == null || translatePhraseItemRequest.getPhraseOptionsUuid().isEmpty()) {
            throw new ValidationException("Translate phrase question must have at least one option");
        }

        if (translatePhraseItemRequest.getPhraseOptionsUuid().size() < 2) {
            throw new ValidationException("Translate phrase question must have at least two options");
        }

        if (translatePhraseItemRequest.getPhraseAnswerUuid() == null) {
            throw new ValidationException("Translate phrase question must have an answer");
        }
    }

    private void validateScenarioPrompt(ScenarioPromptItemRequestDTO scenarioPromptItemRequest) {
        if (scenarioPromptItemRequest.getPhraseUuid() == null) {
            throw new ValidationException("Scenario prompt must have a phrase");
        }

        if (scenarioPromptItemRequest.getOptions() == null || scenarioPromptItemRequest.getOptions().isEmpty()) {
            throw new ValidationException("Scenario prompt must have at least one option");
        }
    }
}
