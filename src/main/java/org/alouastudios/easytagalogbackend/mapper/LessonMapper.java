package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.lesson.LessonRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.QuestionRequestDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonTagalogResponseDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonResponseDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.LessonEnglishResponseDTO;
import org.alouastudios.easytagalogbackend.dto.lesson.QuestionResponseDTO;
import org.alouastudios.easytagalogbackend.model.lessons.Lesson;
import org.alouastudios.easytagalogbackend.model.lessons.Question;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
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

    public void toLessonEntity(LessonRequestDTO lessonRequestDTO, Lesson lesson, Set<Question> questions) {
        lesson.setTitle(lessonRequestDTO.title());
        lesson.setQuestions(questions);
    }

    // This entity mapper is for questions of type "TRANSLATE_WORD"
    public void toQuestionEntity(
            QuestionRequestDTO questionRequestDTO,
            Question question,
            Word word,
            Set<Word> wordOptions) {

        question.setQuestionType(questionRequestDTO.questionType());
        question.setWord(word);
        question.setWordEnglishId(questionRequestDTO.wordEnglishId());
        question.setWordOptions(wordOptions);
        question.setCorrectAnswer(questionRequestDTO.correctAnswer());

        if (questionRequestDTO.helpInfo() != null)
            question.setHelpInfo(questionRequestDTO.helpInfo());
    }

    // This entity mapper is for questions of type "TRANSLATE_PHRASE"
    public void toQuestionEntity(
            QuestionRequestDTO questionRequestDTO,
            Question question,
            Phrase phrase,
            Set<Phrase> phraseOptions) {

        question.setQuestionType(questionRequestDTO.questionType());
        question.setPhrase(phrase);
        question.setPhraseOptions(phraseOptions);
        question.setCorrectAnswer(questionRequestDTO.correctAnswer());

        if (questionRequestDTO.helpInfo() != null)
            question.setHelpInfo(questionRequestDTO.helpInfo());
    }

    public QuestionResponseDTO toQuestionResponseDTO(Question question) {

        // Validation is done before this, so all values that will be null will be checked
        return new QuestionResponseDTO(
                question.getQuestionType(),
                question.getWord() != null
                        ? toLessonEnglishResponseDTO(question.getWord(), question.getWordEnglishId())
                        : null,
                question.getPhrase() != null
                        ? toLessonTagalogResponseDTO(question.getPhrase())
                        : null,
                question.getWordOptions() != null
                        ? question.getWordOptions().stream().map(this::toLessonTagalogResponseDTO).collect(Collectors.toSet())
                        : null,
                question.getPhraseOptions() != null
                        ? question.getPhraseOptions().stream().map(this::toLessonTagalogResponseDTO).collect(Collectors.toSet())
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

    // Only needed for Question Type - TRANSLATE_WORD
    public LessonEnglishResponseDTO toLessonEnglishResponseDTO(Word word, UUID wordEnglishId) {
        return new LessonEnglishResponseDTO(
                word.getUuid(),
                word.getEnglish(wordEnglishId).getMeaning()
        );
    }

    // Only needed for Question type - BUILD_PHRASE
    public LessonEnglishResponseDTO toLessonEnglishResponseDTO(Phrase phrase) {
        return new LessonEnglishResponseDTO(
                phrase.getUuid(),
                phrase.getEnglish()
        );
    }

    // Only needed info in lessons for Word in the frontend is the UUID and tagalog
    public LessonTagalogResponseDTO toLessonTagalogResponseDTO(Word word) {
        return new LessonTagalogResponseDTO(
                word.getUuid(),
                word.getTagalog()
        );
    }

    // Only needed info in lessons for Phrase in the frontend is the UUID and tagalog
    public LessonTagalogResponseDTO toLessonTagalogResponseDTO(Phrase phrase) {
        return new LessonTagalogResponseDTO(
                phrase.getUuid(),
                phrase.getTagalog()
        );
    }
}
