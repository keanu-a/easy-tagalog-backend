package org.alouastudios.easytagalogbackend.controller;

import org.alouastudios.easytagalogbackend.dto.audio.AudioDTO;
import org.alouastudios.easytagalogbackend.service.S3SignedUrlService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final S3SignedUrlService s3SignedUrlService;

    public AudioController(S3SignedUrlService s3SignedUrlService) {
        this.s3SignedUrlService = s3SignedUrlService;
    }

    @PostMapping
    public AudioDTO getSignedAudioUrl(@RequestBody AudioDTO audioRequest) {
        String signedUrl = s3SignedUrlService.generateSignedUrl(audioRequest.audioUrl());
        return new AudioDTO(signedUrl);
    }
}
