package com.protsenko.bankdemo.repository;

import com.protsenko.bankdemo.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Transactional
    @Query("select u from User u where u.username = :username")
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<User> findUserByUsernameWithOptimisticForce(String username);
    @Transactional
    @Query("select u from User u where u.username = :username")
    Optional<User> findUserByUsername(String username);
    @Transactional
    @Query("select count(*) from Phone p join p.user  where p.user.username = :username")
    Long countPhoneFromUser(String username);

    @Transactional
    @Query("select count(*) from Email e join e.user where e.user.username = :username")
    Long countEmailFromUser(String username);

    @Transactional
    @Query("select count(*) from User u")
    Long countUsers();

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsernamePessimicticWrite(String username);

    @Transactional
    @Query("select u.username from User u")
    List<String> getAllUsername();
}