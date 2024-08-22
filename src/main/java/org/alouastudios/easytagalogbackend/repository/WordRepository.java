package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.word.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    List<Word> findByTagalogOrRoot(String tagalog, String root);

    @Query(
            "SELECT w from Word w " +
            "WHERE w.tagalog LIKE %:searchQuery% " +
            "OR w.root LIKE %:searchQuery% " +
            "OR EXISTS (SELECT e FROM w.english e WHERE e.meaning LIKE %:searchQuery%)"
    )
    List<Word> findWordsBySearchQuery(@Param("searchQuery") String searchQuery);
}
