package com.protsenko.bankdemo.controller;


import com.protsenko.bankdemo.dto.response.UserResponseDto;
import com.protsenko.bankdemo.dto.request.*;
import com.protsenko.bankdemo.entity.User;
import com.protsenko.bankdemo.exception.HttpCustomException;
import com.protsenko.bankdemo.security.SecurityUtils;
import com.protsenko.bankdemo.service.UserService;
import jakarta.validation.Valid;
import jdk.jfr.Frequency;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController
{
    private UserService userService;
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid UserRegisterDto userRegisterDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            throw new HttpCustomException(HttpStatus.BAD_REQUEST,bindingResult);
        userService.create(userRegisterDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add_emails")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addEmails(@RequestBody @Valid UserAddingEmailsDto userAddingEmailsDto, BindingResult bindingResult)
    {
        String username = SecurityUtils.getUsername();
        if(bindingResult.hasErrors())
            throw new HttpCustomException(HttpStatus.BAD_REQUEST,bindingResult);
        userService.addEmailsToUser(userAddingEmailsDto.getEmails().toArray(new String[0]), username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add_phones")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addPhones(@RequestBody @Valid UserAddingPhonesDto userAddingPhonesDto, BindingResult bindingResult)
    {
        String username = SecurityUtils.getUsername();
        if(bindingResult.hasErrors())
            throw new HttpCustomException(HttpStatus.BAD_REQUEST,bindingResult);
        userService.addPhonesToUser(userAddingPhonesDto.getPhones().toArray(new String[0]), username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/detach_phone")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> detachPhone(@RequestBody @Valid UserDetachPhoneDto userDetachPhoneDto, BindingResult bindingResult)
    {
        String username = SecurityUtils.getUsername();
        if(bindingResult.hasErrors())
            throw new HttpCustomException(HttpStatus.BAD_REQUEST,bindingResult);
        userService.detachPhoneFromUser(userDetachPhoneDto.getPhone(), username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/detach_email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> detachEmail(@RequestBody @Valid UserDetachEmailDto userDetachEmailDto, BindingResult bindingResult)
    {
        String username = SecurityUtils.getUsername();
        if(bindingResult.hasErrors())
            throw new HttpCustomException(HttpStatus.BAD_REQUEST,bindingResult);
        userService.detachEmailFromUser(userDetachEmailDto.getEmail(), username);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> filter(@RequestParam(required = false) String phone,
                                    @RequestParam(required = false) String email,
                                    @RequestParam(required = false) LocalDate date,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "1") int pageSize
                                    )
    {
        UserFilterDto userFilterDto = new UserFilterDto();
        userFilterDto.setPhone(phone);
        userFilterDto.setEmail(email);
        userFilterDto.setBirthday(date);

        List<User> users = userService.searchUsers(userFilterDto,pageSize, page);
        return ResponseEntity.ok(convertToUserResponseDto(users));
    }

    private List<UserResponseDto> convertToUserResponseDto(List<User> users)
    {
        return users.stream().map(u -> new UserResponseDto(u)).collect(Collectors.toList());
    }
}
