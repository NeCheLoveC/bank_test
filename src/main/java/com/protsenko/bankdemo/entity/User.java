package com.protsenko.bankdemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "User")
@Table(name = "\'user\'")
@Getter
public class User
{
    private static final String GENERATOR_NAME = "users_sequence_gen";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = GENERATOR_NAME)
    private Long id;
    @NotNull(message = "Имя не может быть равно null")
    @Size(min = 5)
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @NotNull
    @Size(min = 5)
    @Column(name = "password", nullable = false)
    private String password;
    @Embedded
    private PersonData personData;
    @Column(name = "money", nullable = false)
    @PositiveOrZero
    private BigDecimal money;
    @Column(name = "created", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "modify")
    @UpdateTimestamp
    private Timestamp lastModify;

    public User(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
