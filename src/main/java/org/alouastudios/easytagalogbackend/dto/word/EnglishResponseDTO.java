package org.alouastudios.easytagalogbackend.dto.word;

import java.util.UUID;

// The only day that should be returned is the meaning
public record EnglishResponseDTO(
        UUID uuid,
        String meaning
) {

}
