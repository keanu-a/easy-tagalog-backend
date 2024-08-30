package org.alouastudios.easytagalogbackend.controller;

import org.alouastudios.easytagalogbackend.dto.PhraseDTO;
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
    public Phrase addPhrase(@RequestBody PhraseDTO phrase) {
        return phraseService.addPhrase(phrase);
    }
}
