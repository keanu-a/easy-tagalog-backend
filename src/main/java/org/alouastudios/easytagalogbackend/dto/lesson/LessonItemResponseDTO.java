package org.alouastudios.easytagalogbackend.dto.lesson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TranslateWordItemResponseDTO.class, name = "translateWord"),
        @JsonSubTypes.Type(value = TranslatePhraseItemResponseDTO.class, name = "translatePhrase")
})
@Getter
@Setter
public abstract class LessonItemResponseDTO {
}