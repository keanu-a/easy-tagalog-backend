package org.alouastudios.easytagalogbackend.model.lessons;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "lesson_questions")
public abstract class LessonQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prompt;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public abstract String getType(); // used to send `type` to frontend

    @Override
    public String toString() {
        return "LessonQuestion{" +
                "id=" + id +
                ", prompt='" + prompt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonQuestion that = (LessonQuestion) o;
        return Objects.equals(id, that.id) && Objects.equals(prompt, that.prompt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prompt);
    }
}
