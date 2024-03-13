package com.protsenko.bankdemo.dto.response;

import com.protsenko.bankdemo.dto.request.UserRegisterDto;
import com.protsenko.bankdemo.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Setter
@Getter
public class UserResponseDto
{
    private String username;
    private LocalDate birthday;
    private Collection<String> emails;
    private Collection<String> phones;
    private String firstname;
    private String lastname;
    private String surname;
    private BigDecimal money;

    public UserResponseDto(User user)
    {
        username = user.getUsername();
        birthday = user.getPersonData().getBirthday();
        emails = user.getEmails().stream().map(e -> e.getEmail()).collect(Collectors.toList());
        phones = user.getPhones().stream().map(p -> p.getPhone()).collect(Collectors.toList());
        firstname = user.getPersonData().getFirstName();
        lastname = user.getPersonData().getLastName();
        surname = user.getPersonData().getSurname();
        money = user.getMoney();
    }
}
