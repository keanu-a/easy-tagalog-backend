package org.alouastudios.easytagalogbackend.validator;

import jakarta.validation.ValidationException;
import org.alouastudios.easytagalogbackend.dto.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.QuestionRequestDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LessonValidator {

    // This function validates the request object
    // First it makes sure there is a "title" field
    // Then it checks the questions field and goes through each question
    public void validateLessonRequest(LessonRequestDTO lessonRequest) {

        if (lessonRequest.title() == null || lessonRequest.title().isEmpty()) {
            throw new ValidationException("Title is required");
        }

        Set<QuestionRequestDTO> questions = lessonRequest.questions();

        if (questions.isEmpty()) {
            throw new ValidationException("Lesson must have at least one question");
        }

        // Goes through each question to make sure it has all required fields based on the question type
        for (QuestionRequestDTO question : questions) {

            switch (question.questionType()) {

                case TRANSLATE_WORD:
                    validateTranslateWordQuestionType(question);
                    break;

                case TRANSLATE_PHRASE:
                    validateTranslatePhraseQuestionType(question);
                    break;

                case BUILD_PHRASE:
                    validateBuildPhraseQuestionType(question);
                    break;

                default:
                    throw new ResourceNotFoundException("No question type was provided");
            }
        }
    }

    // This function is to validate a question of QuestionType "TRANSLATE_WORD"
    // It should have the following:
    // - Word Id
    // - Word Options
    // - Correct Answer
    private void validateTranslateWordQuestionType(QuestionRequestDTO questionRequest) {

        String exceptionPrefix = "Question of type " + questionRequest.questionType() + ": ";

        if (questionRequest.phraseId() != null || questionRequest.phraseOptions() != null) {
            throw new ValidationException(exceptionPrefix + "should not have any phrase fields set");
        }

        if (questionRequest.wordId() == null) {
            throw new ValidationException(exceptionPrefix + "must have a word UUID");
        }

        if (questionRequest.wordOptions() == null ||
                questionRequest.wordOptions().size() != 4) {
            throw new ValidationException(exceptionPrefix + "must have four word options");
        }

        if (questionRequest.correctAnswer() == null) {
            throw new ValidationException(exceptionPrefix + "must have a correct answer");
        }
    }

    // This function is to validate a question of QuestionType "TRANSLATE_PHRASE"
    // It should have the following:
    // - Phrase Id
    // - Phrase Options
    // - Correct Answer
    private void validateTranslatePhraseQuestionType(QuestionRequestDTO questionRequest) {

        String exceptionPrefix = "Question of type " + questionRequest.questionType() + ": ";

        if (questionRequest.wordId() != null || questionRequest.wordOptions() != null) {
            throw new ValidationException(exceptionPrefix + "should not have any word fields set");
        }

        if (questionRequest.phraseId() == null) {
            throw new ValidationException(exceptionPrefix + "must have a phrase UUID");
        }

        if (questionRequest.phraseOptions() == null || questionRequest.phraseOptions().size() != 2) {
            throw new ValidationException(exceptionPrefix + "must have 3 phrase options");
        }

        if (questionRequest.correctAnswer() == null) {
            throw new ValidationException(exceptionPrefix + "must have a correct answer");
        }
    }

    // This function is to validate a question of QuestionType "TRANSLATE_PHRASE"
    // It should have the following:
    // - Phrase Id
    // - Phrase Options
    // - Correct Answer Order
    private void validateBuildPhraseQuestionType(QuestionRequestDTO questionRequest) {
        String exceptionPrefix = "Question of type " + questionRequest.questionType() + ": ";
    }
}
