package com.dictionary.learningcards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int idUser;

    @Size(max = 18, min = 5, message = "Username's length must be between 5 and 18")
    @Column(name = "username")
    private String username;

    @Size(max = 16, min = 8, message = "Password's length must be between 8 and 16")
    @Column(name = "password")
    private String password;

    @OneToMany
    private List<Card> cards;

    @OneToMany
    private List<Group> groups;

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + idUser +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
