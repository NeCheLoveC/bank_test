package com.protsenko.bankdemo;

import com.protsenko.bankdemo.dto.request.UserRegisterDto;
import com.protsenko.bankdemo.entity.PersonData;
import com.protsenko.bankdemo.entity.User;
import com.protsenko.bankdemo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class InitClass
{
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void init()
    {
        PersonData personData1 = new PersonData("","","", LocalDate.of(2000, 2,2));
        UserRegisterDto user1 = new UserRegisterDto();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("admin"));
        user1.setEmail("admin@mail.ru");
        user1.setBirthday(LocalDate.of(2000,2,2));
        user1.setFirstName("Данил");
        user1.setLastName("Иванов");
        user1.setSurname("Иванович");
        user1.setMoney(BigDecimal.valueOf(100));
        user1.setPhone("11111111111");
        userService.create(user1);

        UserRegisterDto user2 = new UserRegisterDto();
        user2.setUsername("client01");
        user2.setPassword(passwordEncoder.encode("admin"));
        user2.setEmail("client@mail.ru");
        user2.setBirthday(LocalDate.of(2005,2,2));
        user2.setFirstName("Данил");
        user2.setLastName("Иванов");
        user2.setSurname("Иванович");
        user2.setMoney(BigDecimal.valueOf(100));
        user2.setPhone("77777777777");
        userService.create(user2);


    }
}
