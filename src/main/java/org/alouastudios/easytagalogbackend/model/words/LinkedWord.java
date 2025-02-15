package org.alouastudios.easytagalogbackend.model.words;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
