package org.alouastudios.easytagalogbackend.model.phrases;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "phrase_grammar_breakdowns")
public class PhraseGrammarBreakdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private String chunk;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phrase_id")
    private Phrase phrase;

    private Boolean isAiGenerated = true;

    @Override
    public String toString() {
        return "PhraseGrammarBreakdown{" +
                "explanation='" + explanation + '\'' +
                ", position='" + position + '\'' +
                ", role='" + role + '\'' +
                ", chunk='" + chunk + '\'' +
                ", isAiGenerated='" + isAiGenerated + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhraseGrammarBreakdown that = (PhraseGrammarBreakdown) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(position, that.position) &&
                Objects.equals(chunk, that.chunk) &&
                Objects.equals(role, that.role) &&
                Objects.equals(explanation, that.explanation) &&
                Objects.equals(isAiGenerated, that.isAiGenerated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, chunk, role, explanation, isAiGenerated);
    }
}
