package org.alouastudios.easytagalogbackend.model.phrases;

import org.alouastudios.easytagalogbackend.enums.NameType;
import org.alouastudios.easytagalogbackend.enums.Tense;

import java.util.UUID;

public record PhraseWordGuide(
        UUID uuid,                  // Required if nameType is null
        UUID englishUuid,           // Required if nameType is null
        Tense tense,
        NameType nameType
) { }
