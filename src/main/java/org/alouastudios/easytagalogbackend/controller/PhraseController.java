package org.alouastudios.easytagalogbackend.controller;

import jakarta.validation.Valid;
import org.alouastudios.easytagalogbackend.dto.PhraseRequestDTO;
import org.alouastudios.easytagalogbackend.dto.response.PhraseResponseDTO;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;
import org.alouastudios.easytagalogbackend.service.PhraseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/phrases")
public class PhraseController {

    private final PhraseService phraseService;

    public PhraseController(PhraseService phraseService) {
        this.phraseService = phraseService;
    }

    @GetMapping
    public List<PhraseResponseDTO> getPhrases() {
        return phraseService.getAllPhrases();
    }

    @GetMapping("/{uuid}")
    public PhraseResponseDTO getPhraseById(@PathVariable UUID uuid) {
        return phraseService.getPhraseById(uuid);
    }

    @PostMapping
    public PhraseResponseDTO addPhrase(@Valid @RequestBody PhraseRequestDTO phraseRequest) {
        return phraseService.addPhrase(phraseRequest);
    }

    @PostMapping("/batch")
    public List<PhraseResponseDTO> addPhraseBatch(@RequestBody List<PhraseRequestDTO> phraseRequests) {
        return phraseService.addPhrases(phraseRequests);
    }

    @PutMapping("/{uuid}")
    public PhraseResponseDTO updatePhrase(@PathVariable UUID uuid, @RequestBody PhraseRequestDTO phraseRequest) {
        return phraseService.updatePhrase(uuid, phraseRequest);
    }

    @DeleteMapping("/{uuid}")
    public String deletePhraseById(@PathVariable UUID uuid) {
        phraseService.deletePhraseById(uuid);
        return "Deleted Phrase UUID: " + uuid;
    }
}
