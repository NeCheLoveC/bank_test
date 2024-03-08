package com.protsenko.bankdemo.security;

import com.protsenko.bankdemo.exception.HttpCustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Component
public class JwtFilter extends OncePerRequestFilter
{
    protected String prefix = "Bearer ";

    protected JwtUtils jwtUtils;

    @Autowired
    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        Jws<Claims> claims = null;

        if(authHeader == null || !authHeader.startsWith(prefix))
        {
            filterChain.doFilter(request,response);
            return;
        }

        token = authHeader.substring(prefix.length());
        try
        {
            claims = jwtUtils.getClaimsFromToken(token);
            if(jwtUtils.isValidJwtToken(claims))
            {
                username = claims.getBody().getSubject();
            }
        }
        catch (ExpiredJwtException e)
        {
            //throw new HttpCustomException(HttpStatus.UNAUTHORIZED, "Просрочен токен.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        catch (SignatureException e)
        {
            //throw new HttpCustomException(HttpStatus.UNAUTHORIZED, "Некорректный токен.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }


        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null && claims != null)
        {
            Authentication authToken = new UsernamePasswordAuthenticationToken(username,null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request,response);
    }
}
