package com.protsenko.bankdemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Phone")
@Table
@Getter
@Setter
public class Phone
{
    private static final String GENERATOR_NAME = "users_sequence_gen";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = GENERATOR_NAME)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "phone", updatable = false, nullable = false, unique = true)
    @NotEmpty
    private String phone;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    protected Phone(){}

    public Phone(String phone, User user) {
        this.phone = phone;
        this.user = user;
    }
}
