package org.alouastudios.easytagalogbackend.mapper;

import org.alouastudios.easytagalogbackend.dto.profile.ProfileRequestDTO;
import org.alouastudios.easytagalogbackend.dto.profile.ProfileResponseDTO;
import org.alouastudios.easytagalogbackend.model.profiles.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileResponseDTO toResponseDTO(Profile profile) {
        return new ProfileResponseDTO(
                profile.getAuthId(),
                profile.getUsername(),
                profile.getEmail(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getLevel(),
                profile.getExp(),
                profile.getStreak()
        );
    }

    public void toEntity(ProfileRequestDTO profileResponseDTO, Profile profile) {
        profile.setAuthId(profileResponseDTO.authId());
        profile.setUsername(profileResponseDTO.username());
        profile.setEmail(profileResponseDTO.email());
        profile.setFirstName(profileResponseDTO.firstName());
        profile.setLastName(profileResponseDTO.lastName());
    }
}
