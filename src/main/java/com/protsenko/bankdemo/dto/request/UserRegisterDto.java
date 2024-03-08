package com.protsenko.bankdemo.dto.request;


import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class UserRegisterDto
{
    @NotNull
    @Size(min = 5)
    private String username;
    @NotNull
    @Size(min = 5)
    private String password;
    @PositiveOrZero
    @NotNull
    private BigDecimal money;
    @NotNull
    @Pattern(regexp = "\\d{11}", message = "Неверный формат телефона")
    private String phone;
    @Email
    @NotNull
    private String email;

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String surname;
    @Past
    private LocalDate birthday;



    public void setUsername(String username) {
        this.username = username.trim();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone)
    {
        this.phone = phone.trim();
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }



    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public void setSurname(String surname) {
        this.surname = surname.trim();
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
