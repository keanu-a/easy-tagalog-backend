package org.alouastudios.easytagalogbackend.model.lessons;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.alouastudios.easytagalogbackend.model.words.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "translate_word_items")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TranslateWordItem extends LessonItem {

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToMany
    @JoinTable(
            name = "translate_word_item_options",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private List<Word> options = new ArrayList<>();

    private UUID answer;

    @Override
    public String getType() {
        return "translateWord";
    }
}
