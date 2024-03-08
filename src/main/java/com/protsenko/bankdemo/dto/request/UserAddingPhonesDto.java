package com.protsenko.bankdemo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserAddingPhonesDto
{
    private List<@NotNull @Pattern(regexp = "\\d{11}") String> phones;
}
