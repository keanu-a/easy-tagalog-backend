package org.alouastudios.easytagalogbackend.model.words;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class English {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private UUID uuid;

    @Column(unique = true, nullable = false)
    private String meaning;

    @ManyToMany(mappedBy = "english")
    @JsonIgnore
    private Set<Word> words = new HashSet<Word>();

    @PrePersist
    public void generateUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }

    @Override
    public String toString() {
        return "English{" +
                "meaning='" + meaning + '\'' +
                ", uuid=" + uuid +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        English english = (English) o;
        return id == english.id &&
                Objects.equals(uuid, english.uuid) &&
                Objects.equals(meaning, english.meaning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, meaning);
    }
}
