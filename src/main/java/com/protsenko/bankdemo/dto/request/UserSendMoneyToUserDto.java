package com.protsenko.bankdemo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UserSendMoneyToUserDto(@NotEmpty String from, @NotEmpty String dest, @NotNull @Positive BigDecimal money) {
}
