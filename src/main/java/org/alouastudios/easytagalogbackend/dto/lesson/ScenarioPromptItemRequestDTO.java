package org.alouastudios.easytagalogbackend.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ScenarioPromptItemRequestDTO extends LessonItemRequestDTO {
    private UUID phraseUuid;
    private List<UUID> options;
}
