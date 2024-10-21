package org.alouastudios.easytagalogbackend.model.words;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

// @Data annotation includes getters, setters, tostring, equals, and hashcode

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "words",
        uniqueConstraints = {@UniqueConstraint(name = "UniqueWordAndAccents", columnNames = {"tagalog", "accents"})}
)
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @Column(length = 30, nullable = false)
    private String tagalog;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "word_english",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "english_id")
    )
    private Set<English> english = new HashSet<>();

    @Column(nullable = false)
    private String root;

    private String accents; // Comma-separated, ex: "1,3,5"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartOfSpeech partOfSpeech;

    private String alternateSpelling; // ex: siya could be sya

    private Boolean isIrregularVerb;

    private String note;

    @Column(unique = true)
    private String audioUrl;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Conjugation> conjugations;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "linked_word_id", referencedColumnName = "id")
    private LinkedWord linkedWord;  // For added ligatures, ex: magandang -- ONLY +ng or +g

    @ManyToMany(mappedBy = "words") // two-way since implement phrases the word is used in later
    @JsonIgnore
    private Set<Phrase> phrases = new HashSet<>();

    @PrePersist
    public void generateUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", tagalog='" + tagalog + '\'' +
                ", root='" + root + '\'' +
                ", accents='" + accents + '\'' +
                ", partOfSpeech=" + partOfSpeech +
                ", alternateSpelling='" + alternateSpelling + '\'' +
                ", isIrregularVerb=" + isIrregularVerb +
                ", note='" + note + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(id, word.id) &&
                Objects.equals(uuid, word.uuid) &&
                Objects.equals(tagalog, word.tagalog) &&
                Objects.equals(root, word.root) &&
                Objects.equals(accents, word.accents) &&
                partOfSpeech == word.partOfSpeech &&
                Objects.equals(alternateSpelling, word.alternateSpelling) &&
                Objects.equals(isIrregularVerb, word.isIrregularVerb) &&
                Objects.equals(note, word.note) &&
                Objects.equals(audioUrl, word.audioUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, tagalog, root, accents, partOfSpeech, alternateSpelling, isIrregularVerb, note, audioUrl);
    }
}
