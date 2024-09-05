package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhraseRepository extends JpaRepository<Phrase, Long> {
}
