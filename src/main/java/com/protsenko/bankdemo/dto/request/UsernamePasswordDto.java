package com.protsenko.bankdemo.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernamePasswordDto
{
    @NotNull(message = "Имя не может быть равно null")
    @Size(min = 5)
    private String username;
    @NotNull
    @Size(min = 5)
    private String password;
}
