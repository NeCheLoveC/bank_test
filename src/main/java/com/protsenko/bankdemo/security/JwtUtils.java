package com.protsenko.bankdemo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils
{
    @Value("${application.security.jwt.secret}")
    protected String secretKey;
    protected UserDetailsService userDetailsService;
    protected Long timeExpired = 1000L * 60L * 30L;

    @Autowired
    public JwtUtils(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @PostConstruct
    private void init()
    {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    //Генерация токена по UserDetails
    public String generateJwt(UserDetails userDetails)
    {
        Date currentDate = new Date();
        Claims claims = Jwts.claims();
        claims.setSubject(userDetails.getUsername());
        claims.setIssuedAt(currentDate);
        Date expiredAt = new Date(currentDate.getTime() + timeExpired);
        claims.setExpiration(expiredAt);
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256,secretKey).compact();
    }



    public Jws<Claims> getClaimsFromToken(String token)
    {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    }

    public boolean isValidJwtToken(Jws<Claims> claims) throws JwtException
    {
        Date expiredAt = claims.getBody().getExpiration();
        if(expiredAt.before(new Date()))
            throw new ExpiredJwtException(claims.getHeader(), claims.getBody(), "Просрочен токен");
        return true;
    }

    private Key getKey()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

}
