package com.dictionary.learningcards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_group")
    private int id;

    @Column(name = "group_name")
    @NotBlank
    private String groupName;

    @ManyToMany(mappedBy = "groups")
    private List<Card> cards;

    public Group() {}

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(int id, String groupName, List<Card> cards) {
        this.id = id;
        this.groupName = groupName;
        this.cards = cards;
    }

    public Group(String groupName, List<Card> cards) {
        this.groupName = groupName;
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Group{" +
                "group='" + groupName + '\'' +
                '}';
    }
}
