package com.dictionary.learningcards.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int id_user;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;
}
