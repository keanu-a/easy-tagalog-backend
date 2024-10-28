package org.alouastudios.easytagalogbackend.model.phrases;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alouastudios.easytagalogbackend.model.words.Word;

import java.util.Objects;
import java.util.Set;
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
    private Boolean isQuestion = false;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "phrase_words",
            joinColumns = @JoinColumn(name = "phrase_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private Set<Word> words;

    @Column(nullable = false, unique = true)
    private String phraseWordMeanings; // ex: "I,name marker,<name>"

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
                ", phraseWordMeanings='" + phraseWordMeanings + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phrase phrase = (Phrase) o;
        return Objects.equals(id, phrase.id) &&
                Objects.equals(uuid, phrase.uuid) &&
                Objects.equals(tagalog, phrase.tagalog) &&
                Objects.equals(english, phrase.english) &&
                Objects.equals(isQuestion, phrase.isQuestion) &&
                Objects.equals(phraseWordMeanings, phrase.phraseWordMeanings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, tagalog, english, isQuestion, phraseWordMeanings);
    }
}
