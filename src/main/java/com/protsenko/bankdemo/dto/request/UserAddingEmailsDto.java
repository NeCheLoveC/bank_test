package com.protsenko.bankdemo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class UserAddingEmailsDto
{
    private List<@Email @NotNull String> emails = new LinkedList<>();
}
