package org.alouastudios.easytagalogbackend.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProfileRequestDTO(
        @NotNull UUID authId,
        @NotBlank String email,
        @NotBlank String name
) {
}
