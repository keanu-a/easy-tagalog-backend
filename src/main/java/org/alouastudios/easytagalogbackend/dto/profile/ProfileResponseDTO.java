package org.alouastudios.easytagalogbackend.dto.profile;

import java.util.UUID;

public record ProfileResponseDTO(
        UUID id,
        String email,
        String name,
        int level,
        int exp,
        int streak
) {
}
