package org.alouastudios.easytagalogbackend.model.words;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "translations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniquePartOfSpeechEnglish",
                        columnNames = {"partOfSpeech", "englishMeanings"}
                )
        }
)
@ToString(exclude = "word")
@EqualsAndHashCode(exclude = "word")
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private UUID uuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartOfSpeech partOfSpeech;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "translation_english",
            joinColumns = @JoinColumn(name = "translation_id"),
            inverseJoinColumns = @JoinColumn(name = "english_id")
    )
    private Set<English> englishMeanings = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "word_id")
    @JsonIgnore
    private Word word;

    @PrePersist
    public void generateUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
