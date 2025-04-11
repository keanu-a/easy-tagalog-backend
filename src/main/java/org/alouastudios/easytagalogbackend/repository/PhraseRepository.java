package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhraseRepository extends JpaRepository<Phrase, Long> {

    Optional<Phrase> findByUuid(UUID uuid);

    List<Phrase> findAllByUuidIn(List<UUID> phrases);

    @Query("SELECT DISTINCT pw.phrase FROM PhraseWord pw WHERE pw.word.uuid = :wordUuid")
    List<Phrase> findTop10ByWordUuid(@Param("wordUuid") UUID wordUuid, Pageable pageable);
}
