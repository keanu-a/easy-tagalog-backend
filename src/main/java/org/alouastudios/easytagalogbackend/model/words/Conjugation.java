package org.alouastudios.easytagalogbackend.model.words;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.alouastudios.easytagalogbackend.enums.Tense;

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "conjugations",
        uniqueConstraints = { @UniqueConstraint(name = "UniqueVerbConjugation", columnNames = {"word_id", "tense"})}
)
@ToString(exclude = "word")
@EqualsAndHashCode(exclude = "word")
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
}
