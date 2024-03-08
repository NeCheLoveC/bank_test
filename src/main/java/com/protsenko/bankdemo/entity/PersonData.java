package com.protsenko.bankdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Embeddable
@Getter
@Setter
public class PersonData
{
    @Column(name = "first_name", nullable = false)
    @NotEmpty
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @NotEmpty
    private String lastName;
    @Column(name = "surname", nullable = false)
    @NotEmpty
    private String surname;
    @Column(name = "birthday", updatable = false)
    @Temporal(TemporalType.DATE)
    @Setter(value = AccessLevel.NONE)
    @Past
    private LocalDate birthday;

    protected PersonData(){}
    public PersonData(String firstName, String lastName, String surname, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.surname = surname;
        this.birthday = birthday;
    }
}
