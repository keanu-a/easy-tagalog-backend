package org.alouastudios.easytagalogbackend.controller;

import org.alouastudios.easytagalogbackend.service.S3SignedUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/audio")
public class AudioController {

    // TODO: Set up signed urls on fetch and on demand

    private final S3SignedUrlService s3SignedUrlService;

    public AudioController(S3SignedUrlService s3SignedUrlService) {
        this.s3SignedUrlService = s3SignedUrlService;
    }

    @GetMapping("/{audioUrl}")
    public ResponseEntity<String> getSignedAudioUrl(@PathVariable String audioUrl) {
        String signedUrl = s3SignedUrlService.generatePresignedUrl(audioUrl);
        return ResponseEntity.ok(signedUrl);
    }
}
