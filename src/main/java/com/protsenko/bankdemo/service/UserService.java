package com.protsenko.bankdemo.service;

import com.protsenko.bankdemo.entity.User;
import com.protsenko.bankdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService
{
    private UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userWrapper = findUserByUsername(username);
        if(userWrapper.isEmpty())
            throw new UsernameNotFoundException(String.format("Пользователь с username = %s не найден", username));
        User user = userWrapper.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(long id)
    {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username)
    {
        return userRepository.findUserByUsername(username);
    }
}
