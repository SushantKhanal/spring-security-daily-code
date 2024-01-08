package com.example.springsecurityclient.entity;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @Column(length = 60)
    private String password;
    private String role;
    private boolean enabled = false;
}


