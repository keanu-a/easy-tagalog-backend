//package org.alouastudios.easytagalogbackend.model.lessons;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
//import org.alouastudios.easytagalogbackend.model.words.Word;
//
//import java.util.HashSet;
//import java.util.Objects;
//import java.util.Set;
//import java.util.UUID;
//
//public class TestLesson {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private UUID uuid;
//
//    @Column(unique = true, nullable = false)
//    private String title;
//
//    // TODO 11/1:
//    //  THINK ABOUT NEW LESSON LOGIC WHERE INSTEAD OF HARD TYPES QUESTIONS, MAKE QUESTIONS BASED OFF LESSON POOL
//    //  WHEN MAKING QUESTIONS, HAVE AN OPTIONAL WORD/PHRASE, AND IF ITS GIVEN USE THAT WORD/PHRASE?
//
//    @ManyToMany
//    @JoinTable(
//            name = "lesson_words",
//            joinColumns = @JoinColumn(name = "lesson_id"),
//            inverseJoinColumns = @JoinColumn(name = "word_id")
//    )
//    private Set<Word> wordPool = new HashSet<>();
//
//    @ManyToMany
//    @JoinTable(
//            name = "lesson_phrases",
//            joinColumns = @JoinColumn(name = "lesson_id"),
//            inverseJoinColumns = @JoinColumn(name = "phrase_id")
//    )
//    private Set<Phrase> phrasePool = new HashSet<>();
//
//    @ManyToMany
//    @JoinTable(
//            name = "lesson_questions",
//            joinColumns = @JoinColumn(name = "lesson_id"),
//            inverseJoinColumns = @JoinColumn(name = "question_id")
//    )
//    private Set<Question> questions = new HashSet<>();
//}
