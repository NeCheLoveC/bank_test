package com.protsenko.bankdemo.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
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
    @JsonSetter("emails")
    private List<@Email @NotNull String> emails = new LinkedList<>();
}
