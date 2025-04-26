package org.alouastudios.easytagalogbackend.dto.lesson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TranslateWordItemResponseDTO.class, name = "translateWord"),
        @JsonSubTypes.Type(value = TranslatePhraseItemResponseDTO.class, name = "translatePhrase"),
        @JsonSubTypes.Type(value = ScenarioPromptItemRequestDTO.class, name = "scenarioPrompt")
})
public abstract class LessonItemResponseDTO {
    private UUID uuid;
}