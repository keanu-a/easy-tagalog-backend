package org.alouastudios.easytagalogbackend.model.phrases;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

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

    @Column(unique = true)
    private String audioUrl;

    @OneToMany(mappedBy = "phrase", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<PhraseWord> phraseWords = new ArrayList<>();

    @OneToMany(mappedBy = "phrase", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<PhraseGrammarBreakdown> grammarBreakdowns = new ArrayList<>();

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
                ", isQuestion=" + isQuestion + '\'' +
                ", audioUrl=" + audioUrl +
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
                Objects.equals(audioUrl, phrase.audioUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, tagalog, english, isQuestion, audioUrl);
    }
}
