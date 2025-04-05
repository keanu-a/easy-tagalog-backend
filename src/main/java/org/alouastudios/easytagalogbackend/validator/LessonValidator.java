package org.alouastudios.easytagalogbackend.validator;

import jakarta.validation.ValidationException;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LessonValidator {

//    // This function validates the request object
//    // First it makes sure there is a "title" field
//    // Then it checks the questions field and goes through each question
//    public void validateLessonRequest(LessonRequestDTO lessonRequest) {
//
//        if (lessonRequest.title() == null || lessonRequest.title().isEmpty()) {
//            throw new ValidationException("Title is required");
//        }
//
//        Set<QuestionRequestDTO> questions = lessonRequest.questions();
//
//        if (questions.isEmpty()) {
//            throw new ValidationException("Lesson must have at least one question");
//        }
//
//        // Goes through each question to make sure it has all required fields based on the question type
//        for (QuestionRequestDTO question : questions) {
//
//            switch (question.questionType()) {
//
//                case TRANSLATE_WORD:
//                    validateTranslateWordQuestionType(question);
//                    break;
//
//                case TRANSLATE_PHRASE:
//                    validateTranslatePhraseQuestionType(question);
//                    break;
//
//                case BUILD_PHRASE:
//                    validateBuildPhraseQuestionType(question);
//                    break;
//
//                default:
//                    throw new ResourceNotFoundException("No question type was provided");
//            }
//        }
//    }
}
