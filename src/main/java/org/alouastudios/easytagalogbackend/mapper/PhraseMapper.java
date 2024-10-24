package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.dto.PhraseWordOrderDTO;
import org.alouastudios.easytagalogbackend.dto.response.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhraseMapper {

    public PhraseResponseDTO toResponseDTO(Phrase phrase, String[] phraseWordMeanings) {
        return new PhraseResponseDTO(
                phrase.getTagalog(),
                phrase.getEnglish(),
                phrase.getIsQuestion(),
                phraseWordMeanings
        );
    }

    public Phrase toEntity(PhraseRequestDTO phraseRequestDTO) {

        Phrase phrase = new Phrase();

        phrase.setTagalog(phraseRequestDTO.tagalog());
        phrase.setEnglish(phraseRequestDTO.english());
        phrase.setIsQuestion(phraseRequestDTO.isQuestion());

        // Maps DTO to a string to concat
        List<String> phraseWordOrder = new ArrayList<>();
        for (PhraseWordOrderDTO order : phraseRequestDTO.phraseWordOrder()) {
            String[] orderString = new String[4];

            orderString[0] = order.id().toString();
            orderString[1] = order.isLinked() ? "+" : "-";
            orderString[2] = order.englishId() != null ? order.englishId().toString() : "";
            orderString[3] = order.tense() != null ? order.tense().toString() : "";

            // Colon separated string
            String concatOrderString = String.join(":", orderString);

            phraseWordOrder.add(concatOrderString);
        }

        // Comma separated string
        phrase.setPhraseWordOrder(String.join(",", phraseWordOrder));

        return phrase;
    }
}
