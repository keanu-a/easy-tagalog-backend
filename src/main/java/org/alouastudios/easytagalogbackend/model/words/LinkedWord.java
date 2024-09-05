package org.alouastudios.easytagalogbackend.model.words;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "linked_words")
public class LinkedWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tagalog;

    @Column(unique = true)
    private String audioUrl;

    @OneToOne(mappedBy = "linkedWord")
    @JsonIgnore
    private Word word;

    @Override
    public String toString() {
        return "LinkedWord{" +
                "id=" + id +
                ", tagalog='" + tagalog + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedWord that = (LinkedWord) o;
        return Objects.equals(id, that.id) && Objects.equals(tagalog, that.tagalog) && Objects.equals(audioUrl, that.audioUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagalog, audioUrl);
    }
}
