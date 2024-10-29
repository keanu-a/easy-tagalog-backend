package org.alouastudios.easytagalogbackend.validator;

import jakarta.validation.ValidationException;
import org.alouastudios.easytagalogbackend.dto.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.QuestionRequestDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LessonValidator {

    public void validateLessonRequest(LessonRequestDTO lessonRequest) {

        Set<QuestionRequestDTO> questions = lessonRequest.questions();

        if (questions.isEmpty()) {
            throw new ValidationException("Lesson must have at least one question");
        }

        // Goes through each question an validates
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

    private void validateTranslateWordQuestionType(QuestionRequestDTO questionRequest) {

        String exceptionPrefix = "Question of type " + questionRequest.questionType() + ": ";

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

    private void validateTranslatePhraseQuestionType(QuestionRequestDTO questionRequest) {

        String exceptionPrefix = "Question of type " + questionRequest.questionType() + ": ";

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

    private void validateBuildPhraseQuestionType(QuestionRequestDTO questionRequest) {
        String exceptionPrefix = "Question of type " + questionRequest.questionType() + ": ";
    }
}
