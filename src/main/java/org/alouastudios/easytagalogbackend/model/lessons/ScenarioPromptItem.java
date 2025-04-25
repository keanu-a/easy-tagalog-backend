package org.alouastudios.easytagalogbackend.model.lessons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "scenario_prompt_items")
@EqualsAndHashCode(callSuper = true)
public class ScenarioPromptItem extends LessonItem {

    @ManyToOne
    @JoinColumn(name = "phrase_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Phrase promptPhrase;

    @ManyToMany
    @JoinTable(
            name = "scenario_prompt_item_options",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Phrase> options = new ArrayList<>();

    @Override
    public String getType() {
        return "scenarioPrompt";
    }
}
