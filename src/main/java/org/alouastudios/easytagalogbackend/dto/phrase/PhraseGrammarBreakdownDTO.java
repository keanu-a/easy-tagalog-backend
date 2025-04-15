package org.alouastudios.easytagalogbackend.dto.phrase;

public record PhraseGrammarBreakdownDTO(
        Integer position,
        String chunk,
        String role,
        String explanation
) {
}
