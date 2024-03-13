package com.protsenko.bankdemo.deposit;

import com.protsenko.bankdemo.entity.User;
import com.protsenko.bankdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DepositManager
{
    private final UserService userService;
    @Value("${bank.max_value_coeff}")
    private BigDecimal max;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void increaseMoney()
    {
        System.out.println("Выдача денег НАЧАТА");
        List<String> users = userService.findAllUsername();
        for(String username : users)
            userService.payForDeposit(username,new BigDecimal("0.05"));
        System.out.println("Выдача денег ОКОНЧЕНА");
    }
}
