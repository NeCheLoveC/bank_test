package com.protsenko.bankdemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity(name = "Email")
@Table
public class Email
{
    private static final String GENERATOR_NAME = "users_sequence_gen";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = GENERATOR_NAME)
    private Long id;
    @Column(name = "email", updatable = false, nullable = false, unique = true)
    @jakarta.validation.constraints.Email
    @NotNull
    private String email;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    protected Email(){}

    public Email(String email, User user) {
        this.email = email;
        this.user = user;
    }
}
