package org.alouastudios.easytagalogbackend.model.words;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alouastudios.easytagalogbackend.enums.Tense;

import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "conjugations",
        uniqueConstraints = { @UniqueConstraint(name = "UniqueVerbConjugation", columnNames = {"word_id", "tense"})}
)
public class Conjugation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String tagalog;

    @Column(nullable = false)
    private String root;

    private String accents; // Comma-separated "1,3,5"

    @Column(unique = true)
    private String audioUrl;

    @Column(nullable = false)
    private String english; // Not using english entity since verb tense has one meaning

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tense tense;

    @ManyToOne
    @JoinColumn(name = "word_id")
    @JsonIgnore
    private Word word;

    @Override
    public String toString() {
        return "Conjugation{" +
                "tense=" + tense +
                ", english='" + english + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", accents='" + accents + '\'' +
                ", root='" + root + '\'' +
                ", tagalog='" + tagalog + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conjugation that = (Conjugation) o;
        return Objects.equals(id, that.id) && Objects.equals(tagalog, that.tagalog) && Objects.equals(root, that.root) && Objects.equals(accents, that.accents) && Objects.equals(audioUrl, that.audioUrl) && Objects.equals(english, that.english) && tense == that.tense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagalog, root, accents, audioUrl, english, tense);
    }
}
