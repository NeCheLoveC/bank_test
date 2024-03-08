package com.protsenko.bankdemo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetachEmailDto
{
    @Email
    @NotNull
    private String email;
}
