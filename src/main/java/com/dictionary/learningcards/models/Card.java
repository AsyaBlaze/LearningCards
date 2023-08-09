package com.dictionary.learningcards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_card")
    private int id;

    @NotEmpty(message = "Should not be empty")
    @Size(min = 2, max = 150, message = "Word's length should be between 2 and 150")
    @Column(name = "word")
    private String word;

    @NotEmpty(message = "Should not be empty")
    @Size(min = 2, max = 200, message = "Translation's length should be between 2 and 150")
    @Column(name = "translation")
    private String translation;

    @Column(name = "is_learned")
    private boolean isLearned;

    @Column(name = "examples")
    private String examples;

    @Column(name = "transcription")
    private String transcription;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "cards_groups",
        joinColumns = @JoinColumn(name = "id_card"),
        inverseJoinColumns = @JoinColumn(name = "id_group"))
    private List<Group> groups;

}
