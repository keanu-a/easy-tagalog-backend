package org.alouastudios.easytagalogbackend.model.words;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;

import java.util.HashSet;
import java.util.Objects;
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

    @Override
    public String toString() {
        return "Translation{" +
                "partOfSpeech=" + partOfSpeech +
                ", uuid=" + uuid +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Translation that = (Translation) o;
        return id == that.id && Objects.equals(uuid, that.uuid) && partOfSpeech == that.partOfSpeech;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, partOfSpeech);
    }
}
