package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.QuestionRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.lessonResponse.LessonPhraseResponseDTO;
import org.alouastudios.easytagalogbackend.dto.response.lessonResponse.LessonResponseDTO;
import org.alouastudios.easytagalogbackend.dto.response.lessonResponse.LessonWordResponseDTO;
import org.alouastudios.easytagalogbackend.dto.response.lessonResponse.QuestionResponseDTO;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.model.lessons.Question;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LessonMapper {

    public LessonResponseDTO toResponseDTO(Lesson lesson) {
        return new LessonResponseDTO(
                lesson.getUuid(),
                lesson.getTitle(),
                lesson.getQuestions().stream().map(this::toQuestionResponseDTO).collect(Collectors.toSet())
        );
    }

    // TODO 10/28: WORK ON LESSON DTO MAPPING TO ENTITY
    public void toLessonEntity(LessonRequestDTO lessonRequestDTO, Lesson lesson) {

    }

    public void toQuestionEntity(QuestionRequestDTO questionRequestDTO, Question question) {
    }

    public QuestionResponseDTO toQuestionResponseDTO(Question question) {

        // Validation is done before this, so all values that will be null will be checked
        return new QuestionResponseDTO(
                question.getQuestionType(),
                question.getWord() != null
                        ? toLessonWordResponseDTO(question.getWord())
                        : null,
                question.getPhrase() != null
                        ? toLessonPhraseResponseDTO(question.getPhrase())
                        : null,
                question.getWordOptions() != null
                        ? question.getWordOptions().stream().map(this::toLessonWordResponseDTO).collect(Collectors.toSet())
                        : null,
                question.getPhraseOptions() != null
                        ? question.getPhraseOptions().stream().map(this::toLessonPhraseResponseDTO).collect(Collectors.toSet())
                        : null,
                question.getCorrectAnswer() != null
                        ? question.getCorrectAnswer()
                        : null,
                question.getCorrectAnswerOrder() != null
                        ? question.getCorrectAnswerOrder()
                        : null,
                question.getHelpInfo() != null
                        ? question.getHelpInfo()
                        : null
        );
    }

    // Only needed info in lessons for Word in the frontend is the UUID and tagalog
    public LessonWordResponseDTO toLessonWordResponseDTO(Word word) {
        return new LessonWordResponseDTO(
                word.getUuid(),
                word.getTagalog()
        );
    }

    // Only needed info in lessons for Phrase in the frontend is the UUID and tagalog
    public LessonPhraseResponseDTO toLessonPhraseResponseDTO(Phrase phrase) {
        return new LessonPhraseResponseDTO(
                phrase.getUuid(),
                phrase.getTagalog()
        );
    }
}
