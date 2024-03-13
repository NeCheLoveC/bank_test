package com.protsenko.bankdemo.controller;

import com.protsenko.bankdemo.dto.request.UsernamePasswordDto;
import com.protsenko.bankdemo.entity.User;
import com.protsenko.bankdemo.exception.HttpCustomException;
import com.protsenko.bankdemo.security.JwtUtils;
import com.protsenko.bankdemo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/jwt")
@RequiredArgsConstructor
public class JwtController
{
    private final UserService userService;
    private final JwtUtils jwtUtils;
    @Autowired
    @Qualifier("passwordEncoder")
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @PostMapping
    public ResponseEntity<?> generateJwt(@RequestBody @Valid UsernamePasswordDto usernamePasswordDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            throw new HttpCustomException(HttpStatus.BAD_REQUEST, "Неверный  логин / пароль");
        Optional<User> userWrapper = userService.findUserByUsername(usernamePasswordDto.getUsername());
        userWrapper.orElseThrow(() -> new HttpCustomException(HttpStatus.BAD_REQUEST, "Неверный  логин / пароль"));
        User user = userWrapper.get();
        if(!bCryptPasswordEncoder.matches(usernamePasswordDto.getPassword(), user.getPassword()))
            throw new HttpCustomException(HttpStatus.BAD_REQUEST, "Неверный  логин / пароль");
        String token = jwtUtils.generateJwt(user);
        return ResponseEntity.ok().body(new HashMap.SimpleImmutableEntry("token",token));
    }
}
