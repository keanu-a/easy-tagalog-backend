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

    public ProfileResponseDTO getProfileById(UUID id) {
        Profile foundProfile = profileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return profileMapper.toResponseDTO(foundProfile);
    }

    @Transactional
    public ProfileResponseDTO updateProfile(UUID id, ProfileRequestDTO profileRequest) {
        Profile foundProfile = profileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Updating email is done through Supabase, not backend API
        if (profileRequest.email() != null && !profileRequest.email().equals(foundProfile.getEmail())) {
            throw new IllegalArgumentException("Can not change email");
        }

        profileMapper.toEntity(profileRequest, foundProfile);

        profileRepository.save(foundProfile);

        return profileMapper.toResponseDTO(foundProfile);
    }

    public void deleteProfile(UUID id) {
        Profile foundProfile = profileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        profileRepository.delete(foundProfile);
    }
}
