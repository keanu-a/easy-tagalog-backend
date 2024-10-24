package org.alouastudios.easytagalogbackend.controller;

import org.alouastudios.easytagalogbackend.dto.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.service.PhraseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phrases")
public class PhraseController {

    private final PhraseService phraseService;

    public PhraseController(PhraseService phraseService) {
        this.phraseService = phraseService;
    }

    @GetMapping
    public List<Phrase> getPhrases() {
        return phraseService.getAllPhrases();
    }

    @GetMapping("/{id}")
    public Phrase getPhraseById(@PathVariable long id) {
        return phraseService.getPhraseById(id);
    }

    @PostMapping
    public Phrase addPhrase(@RequestBody PhraseRequestDTO phrase) {
        return phraseService.addPhrase(phrase);
    }

    @PostMapping("/batch")
    public List<Phrase> addPhraseBatch(@RequestBody List<PhraseRequestDTO> phrases) {
        return phraseService.addPhrases(phrases);
    }

    // TODO: PUT request
    @PutMapping("/{id}")
    public Phrase updatePhrase(@PathVariable long id, @RequestBody PhraseRequestDTO phrase) {
        return phraseService.updatePhrase(id, phrase);
    }

    @DeleteMapping("/{id}")
    public String deletePhraseById(@PathVariable long id) {
        phraseService.deletePhraseById(id);
        return "Deleted Phrase Id: " + id;
    }
}
