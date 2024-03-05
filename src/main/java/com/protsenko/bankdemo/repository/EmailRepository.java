package com.protsenko.bankdemo.repository;

import com.protsenko.bankdemo.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long>
{
    @Transactional
    public Optional<Email> findByEmail(String email);
}
