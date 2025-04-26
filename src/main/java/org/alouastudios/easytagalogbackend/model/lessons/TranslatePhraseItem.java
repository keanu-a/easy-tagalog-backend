package org.alouastudios.easytagalogbackend.model.lessons;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.alouastudios.easytagalogbackend.model.phrases.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "translate_phrase_items")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TranslatePhraseItem extends LessonItem {

    @ManyToOne
    @JoinColumn(name = "phrase_id")
    private Phrase phrase;

    @ManyToMany
    @JoinTable(
            name = "translate_phrase_item_options",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_id")
    )
    private List<Phrase> options = new ArrayList<>();

    private UUID answer;

    @Override
    public String getType() {
        return "translatePhrase";
    }
}
