package org.alouastudios.easytagalogbackend.controller;

import org.alouastudios.easytagalogbackend.dto.WordRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.WordResponseDTO;
import org.alouastudios.easytagalogbackend.model.words.Word;
import org.alouastudios.easytagalogbackend.service.WordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/words")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping
    public List<WordResponseDTO> getAllWords() {
        return wordService.getAllWords();
    };

    @GetMapping("/{uuid}")
    public WordResponseDTO getWordByUUID(@PathVariable UUID uuid) {
        return wordService.getWordByUUID(uuid);
    };

    @GetMapping("/search/{searchQuery}")
    public List<WordResponseDTO> searchWord(@PathVariable String searchQuery) {
        return wordService.getWordsBySearchQuery(searchQuery);
    }

    @PostMapping
    public WordResponseDTO addWord(@RequestBody WordRequestDTO word) {
        return wordService.addWord(word);
    };

    @PostMapping("/batch")
    public List<WordResponseDTO> addWords(@RequestBody List<WordRequestDTO> words) {
        return wordService.addWords(words);
    }

    @PutMapping("/{uuid}")
    public WordResponseDTO updateWord(@PathVariable UUID uuid, @RequestBody WordRequestDTO word) {
        return wordService.updateWord(uuid, word);
    };

    @DeleteMapping("/{uuid}")
    public String deleteWord(@PathVariable UUID uuid) {
        wordService.deleteWord(uuid);
        return "Word deleted";
    };
}
