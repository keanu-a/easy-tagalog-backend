package org.alouastudios.easytagalogbackend.controller;

import org.alouastudios.easytagalogbackend.dto.profile.ProfileRequestDTO;
import org.alouastudios.easytagalogbackend.dto.profile.ProfileResponseDTO;
import org.alouastudios.easytagalogbackend.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<ProfileResponseDTO> getAllProfiles() {
        return profileService.getAllUsers();
    }

    @GetMapping("/{uuid}")
    public ProfileResponseDTO getProfile(@PathVariable UUID uuid) {
        return profileService.getProfileById(uuid);
    }

    @PutMapping("/{uuid}")
    public ProfileResponseDTO updateProfile(@PathVariable UUID uuid, @RequestBody ProfileRequestDTO profileRequest) {
        return profileService.updateProfile(uuid, profileRequest);
    }

    @DeleteMapping("/{uuid}")
    public String deleteProfile(@PathVariable UUID uuid) {
        profileService.deleteProfile(uuid);
        return "Deleted Phrase UUID: " + uuid;
    }
}
