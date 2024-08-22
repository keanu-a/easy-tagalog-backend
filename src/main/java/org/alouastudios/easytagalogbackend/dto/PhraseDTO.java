package org.alouastudios.easytagalogbackend.dto;

import org.alouastudios.easytagalogbackend.model.word.Word;

import java.util.List;

public record PhraseDTO (
        Long id,
        String tagalog,
        String english,
        List<Word> words
) {
}
