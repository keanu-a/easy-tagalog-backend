package org.alouastudios.easytagalogbackend.model.words;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;
import org.alouastudios.easytagalogbackend.enums.Tense;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;

import java.util.*;

// @Data annotation includes getters, setters, tostring, equals, and hashcode

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "words",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueWordAndAccents", columnNames = {"tagalog", "accents"})
        }
)
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid; // Use this if need to expose to the frontend

    @Column(length = 30, nullable = false)
    private String tagalog;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Translation> translations = new HashSet<>();

    @Column(nullable = false)
    private String root;

    private String accents; // Comma-separated, ex: "1,3,5"

    private String alternateSpelling; // ex: siya could be sya

    private Boolean isIrregularVerb;

    private String note;

    @Column(unique = true)
    private String audioUrl;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Conjugation> conjugations = new HashSet<>();

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

    @Transient
    public Conjugation getConjugation(Tense tense) {
        return conjugations.stream()
                .filter(conjugation -> conjugation.getTense().equals(tense))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No conjugation found for " + tagalog + " with tense: " + tense
                ));
    }

    @Transient
    public boolean isVerb() {
        for (Translation translation : translations) {
            if (translation.getPartOfSpeech() == PartOfSpeech.VERB) {
                return true;
            }
        }
        return false;
    }
}
