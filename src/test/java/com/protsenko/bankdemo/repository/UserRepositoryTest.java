package com.protsenko.bankdemo.repository;

import com.protsenko.bankdemo.InitClass;
import com.protsenko.bankdemo.entity.User;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    private UserRepository userRepository;
    @BeforeAll
    public void init()
    {

    }

    @Test
    void findUserByUsernameWithOptimisticForce()
    {
        Optional<User> userWrapper = userRepository.findUserByUsernameWithOptimisticForce("admin");

        assertThat(userWrapper.isPresent()).isTrue();
        User user = userWrapper.get();
        assertThat(user.getUsername()).isEqualTo("admin");
    }

    @Test
    void findUserByUsername() {
    }

    @Test
    void countPhoneFromUser() {
    }

    @Test
    void countEmailFromUser() {
    }

    @Test
    void countUsers() {
    }
}