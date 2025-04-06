package org.alouastudios.easytagalogbackend.model.phrases;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alouastudios.easytagalogbackend.model.words.Word;

@Data
@NoArgsConstructor
@Entity
@Table(name = "phraseWords")
public class PhraseWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer position;

    private String english;

    private String note;

    @Column(nullable = false)
    private Boolean isProperNoun = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Word word;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phrase_id")
    private Phrase phrase;
}
