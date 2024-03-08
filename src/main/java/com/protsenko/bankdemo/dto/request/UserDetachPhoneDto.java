package com.protsenko.bankdemo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetachPhoneDto
{
    @NotNull
    @Pattern(regexp = "\\d{11}")
    private String phone;
}
