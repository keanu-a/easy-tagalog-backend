package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.words.English;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EnglishRepository extends JpaRepository<English, Long> {

    Set<English> findByMeaningIn(Set<String> english);

    Optional<English> findByUuid(UUID uuid);
}
