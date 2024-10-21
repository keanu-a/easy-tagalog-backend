package org.alouastudios.easytagalogbackend.model.phrases;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alouastudios.easytagalogbackend.model.words.Word;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "phrases")
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String tagalog;

    @Column(nullable = false)
    private String english;

    @Column(nullable = false)
    private Boolean isQuestion;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "phrase_words",
            joinColumns = @JoinColumn(name = "phrase_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private List<Word> words;

    @Column(nullable = false, unique = true)
    private String wordIdLinkedMeaningConjugationOrder; // ex: "3:-:moon:-,5:+:-:-,2:-:-:PAST,3:+:-:-

    @PrePersist
    public void generateUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }

    @Override
    public String toString() {
        return "Phrase{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", tagalog='" + tagalog + '\'' +
                ", english='" + english + '\'' +
                ", isQuestion=" + isQuestion +
                ", wordIdLinkedMeaningConjugationOrder='" + wordIdLinkedMeaningConjugationOrder + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phrase phrase = (Phrase) o;
        return Objects.equals(id, phrase.id) && Objects.equals(uuid, phrase.uuid) && Objects.equals(tagalog, phrase.tagalog) && Objects.equals(english, phrase.english) && Objects.equals(isQuestion, phrase.isQuestion) && Objects.equals(wordIdLinkedMeaningConjugationOrder, phrase.wordIdLinkedMeaningConjugationOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, tagalog, english, isQuestion, wordIdLinkedMeaningConjugationOrder);
    }
}
