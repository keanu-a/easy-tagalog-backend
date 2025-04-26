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
        if (translateWordItemRequest.getWord() == null) {
            throw new ValidationException("Translate word question must have a word");
        }

        if (translateWordItemRequest.getOptions() == null || translateWordItemRequest.getOptions().isEmpty()) {
            throw new ValidationException("Translate word question must have at least one option");
        }

        if (translateWordItemRequest.getOptions().size() < 2) {
            throw new ValidationException("Translate word question must have at least two options");
        }

        if (translateWordItemRequest.getAnswer() == null) {
            throw new ValidationException("Translate word question must have an answer");
        }
    }

    private void validateTranslatePhraseQuestion(TranslatePhraseItemRequestDTO translatePhraseItemRequest) {
        if (translatePhraseItemRequest.getPhrase() == null) {
            throw new ValidationException("Translate phrase question must have a word");
        }

        if (translatePhraseItemRequest.getOptions() == null || translatePhraseItemRequest.getOptions().isEmpty()) {
            throw new ValidationException("Translate phrase question must have at least one option");
        }

        if (translatePhraseItemRequest.getOptions().size() < 2) {
            throw new ValidationException("Translate phrase question must have at least two options");
        }

        if (translatePhraseItemRequest.getAnswer() == null) {
            throw new ValidationException("Translate phrase question must have an answer");
        }
    }

    private void validateScenarioPrompt(ScenarioPromptItemRequestDTO scenarioPromptItemRequest) {
        if (scenarioPromptItemRequest.getPromptPhrase() == null) {
            throw new ValidationException("Translate phrase question must have a word");
        }

        if (scenarioPromptItemRequest.getOptions() == null || scenarioPromptItemRequest.getOptions().isEmpty()) {
            throw new ValidationException("Scenario prompt must have at least one option");
        }

        if (scenarioPromptItemRequest.getOptions().size() < 2) {
            throw new ValidationException("Scenario prompt must have at least two options");
        }
    }
}
