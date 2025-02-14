package org.alouastudios.easytagalogbackend.dto.word;

import org.alouastudios.easytagalogbackend.enums.PartOfSpeech;

import java.util.Set;

public record TranslationResponseDTO (
   PartOfSpeech partOfSpeech,
   Set<EnglishResponseDTO> englishMeanings
) {}
