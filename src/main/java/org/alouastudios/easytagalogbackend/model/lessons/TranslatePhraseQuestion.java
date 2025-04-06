package org.alouastudios.easytagalogbackend.model.lessons;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "translate_phrase_questions")
public class TranslatePhraseQuestion extends LessonQuestion {

    @ManyToMany
    @JoinTable(
            name = "translate_phrase_question_options",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_id")
    )
    private List<Phrase> options = new ArrayList<>();

    private UUID answer;

    @Override
    public String getType() {
        return "translatePhrase";
    }

    @Override
    public String toString() {
        return "TranslatePhraseQuestion{" +
                "answer=" + answer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TranslatePhraseQuestion that = (TranslatePhraseQuestion) o;
        return Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), answer);
    }
}
