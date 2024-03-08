package com.protsenko.bankdemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity(name = "User")
@Table(name = "\'user\'")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User implements UserDetails
{
    private static final String GENERATOR_NAME = "users_sequence_gen";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = GENERATOR_NAME)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Version
    private Long version;
    @NotNull(message = "Имя не может быть равно null")
    @Size(min = 5)
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @NotNull
    @Size(min = 5)
    @Column(name = "password", nullable = false)
    private String password;
    @Embedded
    private PersonData personData = new PersonData();
    @Column(name = "money", nullable = false)
    @PositiveOrZero
    @NotNull
    private BigDecimal money;
    @Column(name = "start_money_on_deposit", updatable = false, nullable = false)
    @PositiveOrZero
    @NotNull
    private BigDecimal startMoneyOnDeposit;
    @Column(name = "created", updatable = false, nullable = false)
    @CreationTimestamp
    @Setter(AccessLevel.PRIVATE)
    private Timestamp createdAt;
    @Column(name = "modify", nullable = false)
    @UpdateTimestamp
    @Setter(AccessLevel.PRIVATE)
    private Timestamp lastModify;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private Collection<Phone> phones = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private Collection<Email> emails = new ArrayList<>();
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
