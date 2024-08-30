package org.alouastudios.easytagalogbackend.model.phrases;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alouastudios.easytagalogbackend.model.words.Word;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "phrases")
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tagalog;

    @Column(nullable = false)
    private String english;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "phrase_words",
            joinColumns = @JoinColumn(name = "phrase_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private List<Word> words;

    @Column(nullable = false)
    private String wordOrder; // ex: "0,1,2:PAST,0

    @Override
    public String toString() {
        return "Phrase{" +
                "id=" + id +
                ", tagalog='" + tagalog + '\'' +
                ", english='" + english + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phrase phrase = (Phrase) o;
        return Objects.equals(id, phrase.id) && Objects.equals(tagalog, phrase.tagalog) && Objects.equals(english, phrase.english);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagalog, english);
    }
}
