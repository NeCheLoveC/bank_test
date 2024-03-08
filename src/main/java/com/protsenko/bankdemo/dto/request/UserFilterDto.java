package com.protsenko.bankdemo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class UserFilterDto
{
    private LocalDate birthday;
    private String phone;
    private String firstname;
    private String surname;
    private String lastname;
    private String email;

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setPhone(String phone) {
        if(phone != null)
            this.phone = phone.trim();
    }

    public void setFirstname(String firstname) {
        if(firstname != null)
            this.firstname = firstname.trim();
    }

    public void setSurname(String surname) {
        if(surname != null)
            this.surname = surname.trim();
    }

    public void setLastname(String lastname) {
        if(lastname != null)
            this.lastname = lastname.trim();
    }

    public void setEmail(String email) {
        if(email != null)
            this.email = email.trim();
    }
}
