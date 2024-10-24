package org.alouastudios.easytagalogbackend.model.lessons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alouastudios.easytagalogbackend.enums.QuestionType;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.model.words.Word;

import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "questions"
)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "word_id")
    @JsonIgnore
    private Word word;

    @ManyToOne
    @JoinColumn(name = "phrase_id")
    @JsonIgnore
    private Phrase phrase;

    @ManyToMany
    @JoinTable(
            name = "question_word_options",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private Set<Word> wordOptions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "question_phrase_options",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_id")
    )
    private List<Phrase> phraseOptions;

    private UUID correctAnswer;

    @ElementCollection
    private List<UUID> correctAnswerOrder;

    private String helpInfo;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionType=" + questionType +
                ", correctAnswer=" + correctAnswer +
                ", correctAnswerOrder=" + correctAnswerOrder +
                ", helpInfo='" + helpInfo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) && questionType == question.questionType && Objects.equals(correctAnswer, question.correctAnswer) && Objects.equals(correctAnswerOrder, question.correctAnswerOrder) && Objects.equals(helpInfo, question.helpInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionType, correctAnswer, correctAnswerOrder, helpInfo);
    }
}
