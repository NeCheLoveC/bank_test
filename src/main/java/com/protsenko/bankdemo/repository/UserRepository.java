package com.protsenko.bankdemo.repository;

import com.protsenko.bankdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Transactional
    Optional<User> findUserByUsername(String username);
}