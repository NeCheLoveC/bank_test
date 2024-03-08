package com.protsenko.bankdemo.deposit;

import com.protsenko.bankdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DepositManager
{
    private final UserService userService;
    @Value("${bank.max_value_coeff}")
    private BigDecimal max;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void increaseMoney()
    {

    }
}
