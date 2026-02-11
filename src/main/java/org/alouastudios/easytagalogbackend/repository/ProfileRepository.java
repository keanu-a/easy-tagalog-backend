package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.profiles.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findById(UUID id);
}
