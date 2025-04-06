package org.alouastudios.easytagalogbackend.dto.lesson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TranslateWordQuestionResponseDTO.class, name = "translateWord"),
        @JsonSubTypes.Type(value = TranslatePhraseQuestionResponseDTO.class, name = "translatePhrase")
})
@Getter
@Setter
public abstract class LessonQuestionResponseDTO {
    protected String prompt;

    public LessonQuestionResponseDTO(String prompt) {
        this.prompt = prompt;
    }
}