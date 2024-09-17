package org.alouastudios.easytagalogbackend.controller;

import org.alouastudios.easytagalogbackend.dto.WordDTO;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.service.WordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/words")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping
    public List<Word> getAllWords() {
        return wordService.getAllWords();
    };

    @GetMapping("/{id}")
    public Word getWordById(@PathVariable Long id) {
        return wordService.getWordById(id);
    };

    @GetMapping("/search/{searchQuery}")
    public List<Word> searchWord(@PathVariable String searchQuery) {
        return wordService.getWordsBySearchQuery(searchQuery);
    }

    @PostMapping
    public Word addWord(@RequestBody WordDTO word) {
        return wordService.addWord(word);
    };

    @PostMapping("/batch")
    public List<Word> addWords(@RequestBody List<WordDTO> words) {
        return wordService.addWords(words);
    }

    @PutMapping("/{id}")
    public Word updateWord(Word word) {
        return wordService.updateWord(word);
    };

    @DeleteMapping("/{id}")
    public String deleteWord(@PathVariable Long id) {
        wordService.deleteWord(id);
        return "Deleted Job Id: " + id;
    };
}
