package com.protsenko.bankdemo.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils
{
    public static String getUsername()
    {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
