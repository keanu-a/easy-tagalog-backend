package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhraseRepository extends JpaRepository<Phrase, Long> {

    Optional<Phrase> findByUuid(UUID uuid);

    List<Phrase> findAllByUuidIn(List<UUID> phrases);
}
