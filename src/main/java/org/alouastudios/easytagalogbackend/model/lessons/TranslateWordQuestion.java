package org.alouastudios.easytagalogbackend.model.lessons;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alouastudios.easytagalogbackend.model.words.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "translate_word_questions")
public class TranslateWordQuestion extends LessonQuestion {

    @ManyToMany
    @JoinTable(
            name = "translate_word_question_options",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private List<Word> options = new ArrayList<>();

    private UUID answer;

    @Override
    public String getType() {
        return "translateWord";
    }

    @Override
    public String toString() {
        return "TranslateWordQuestion{" +
                "answer=" + answer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TranslateWordQuestion that = (TranslateWordQuestion) o;
        return Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), answer);
    }
}
