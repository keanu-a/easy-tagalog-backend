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
        @JsonSubTypes.Type(value = TranslateWordQuestionRequestDTO.class, name = "translateWord"),
        @JsonSubTypes.Type(value = TranslatePhraseQuestionRequestDTO.class, name = "translatePhrase")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class LessonQuestionRequestDTO {
    protected String prompt;
}
