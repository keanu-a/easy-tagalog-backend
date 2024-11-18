package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.lesson.*;
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

        System.out.println(question.getWordOptions());
        System.out.println(question.getPhraseOptions());

        // Validation is done before this, so all values that will be null will be checked
        return new QuestionResponseDTO(
                question.getQuestionType(),
                question.getWord() != null
                        ? toPromptResponseDTO(question.getWord(), question.getWordEnglishId())
                        : toPromptResponseDTO(question.getPhrase()),
                question.getWordOptions().isEmpty()
                        ? question.getPhraseOptions().stream().map(this::toLessonOptionDTO).collect(Collectors.toSet())
                        : question.getWordOptions().stream().map(this::toLessonOptionDTO).collect(Collectors.toSet()),
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

    public LessonPromptResponseDTO toPromptResponseDTO(Word word, UUID wordEnglishId) {
        return new LessonPromptResponseDTO(
            word.getEnglish(wordEnglishId).getMeaning(),
            word.getTagalog(),
            false
        );
    }

    public LessonPromptResponseDTO toPromptResponseDTO(Phrase phrase) {
        return new LessonPromptResponseDTO(
                phrase.getEnglish(),
                phrase.getTagalog(),
                phrase.getIsQuestion()
        );
    }

    public LessonOptionResponseDTO toLessonOptionDTO(Word word) {
        return new LessonOptionResponseDTO(
                word.getUuid(),
                word.getTagalog()
        );
    }

    public LessonOptionResponseDTO toLessonOptionDTO(Phrase phrase) {
        return new LessonOptionResponseDTO(
                phrase.getUuid(),
                phrase.getTagalog()
        );
    }
}
