package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.words.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByUuid(UUID uuid);

    List<Word> findAllByUuidIn(List<UUID> uuids);

    @Query("""
        SELECT DISTINCT w FROM Word w
        JOIN w.translations t
        JOIN t.englishMeanings e
        WHERE LOWER(w.tagalog) = LOWER(:searchQuery)
        OR LOWER(w.root) = LOWER(:searchQuery)
        OR LOWER(e.meaning) = LOWER(:searchQuery)
        OR LOWER(e.meaning) LIKE LOWER(CONCAT(:searchQuery, '%'))
    """)
    List<Word> findWordsBySearchQuery(@Param("searchQuery") String searchQuery);
}
