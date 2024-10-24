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

    @Query(
            "SELECT w from Word w " +
            "WHERE w.tagalog LIKE %:searchQuery% " +
            "OR w.root LIKE %:searchQuery% " +
            "OR EXISTS (SELECT e FROM w.english e WHERE e.meaning LIKE %:searchQuery%)"
    )
    List<Word> findWordsBySearchQuery(@Param("searchQuery") String searchQuery);
}
