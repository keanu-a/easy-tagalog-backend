package org.alouastudios.easytagalogbackend.dto.lesson;

public record LessonPromptResponseDTO(
        String tagalog,
        String english,
        Boolean isPhraseQuestion // This is needed for phrases that ask a question
) {
}
