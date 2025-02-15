package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.words.English;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EnglishRepository extends JpaRepository<English, Long> {

    English findByMeaning(String english);

    Set<English> findByMeaningIn(Set<String> english);
}
