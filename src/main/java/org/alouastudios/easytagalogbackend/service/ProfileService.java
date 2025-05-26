package org.alouastudios.easytagalogbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.dto.profile.ProfileRequestDTO;
import org.alouastudios.easytagalogbackend.dto.profile.ProfileResponseDTO;
import org.alouastudios.easytagalogbackend.exception.ResourceNotFoundException;
import org.alouastudios.easytagalogbackend.mapper.ProfileMapper;
import org.alouastudios.easytagalogbackend.model.profiles.Profile;
import org.alouastudios.easytagalogbackend.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public List<ProfileResponseDTO> getAllUsers() {
        return profileRepository.findAll().stream().map(profileMapper::toResponseDTO).toList();
    }

    public ProfileResponseDTO getProfileByUUID(UUID uuid) {
        Profile foundProfile = profileRepository.findByAuthId(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return profileMapper.toResponseDTO(foundProfile);
    }

    @Transactional
    public ProfileResponseDTO createProfile(ProfileRequestDTO profileRequestDTO) {
        Profile newProfile = new Profile();

        profileMapper.toEntity(profileRequestDTO, newProfile);

        profileRepository.save(newProfile);

        return profileMapper.toResponseDTO(newProfile);
    }

    @Transactional
    public ProfileResponseDTO updateProfile(UUID uuid, ProfileRequestDTO profileRequest) {
        Profile foundProfile = profileRepository.findByAuthId(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Updating email is done through Supabase, not backend API
        if (!profileRequest.email().equals(foundProfile.getEmail())) {
            throw new IllegalArgumentException("Can not change email");
        }

        profileMapper.toEntity(profileRequest, foundProfile);

        profileRepository.save(foundProfile);

        return profileMapper.toResponseDTO(foundProfile);
    }

    public void deleteProfile(UUID uuid) {
        Profile foundProfile = profileRepository.findByAuthId(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        profileRepository.delete(foundProfile);
    }
}
