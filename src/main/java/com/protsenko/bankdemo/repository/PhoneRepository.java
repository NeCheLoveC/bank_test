package com.protsenko.bankdemo.repository;

import com.protsenko.bankdemo.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long>
{
    @Transactional
    public Optional<Phone> findByPhone(String phone);
}
