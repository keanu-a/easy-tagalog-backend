package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.words.English;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnglishRepository extends JpaRepository<English, Long> {

    English findByMeaning(String english);
}
