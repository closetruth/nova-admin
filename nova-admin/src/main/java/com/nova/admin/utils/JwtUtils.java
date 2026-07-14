package com.nova.admin.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    private final String secretKey;
    private final long expireTime;

    public JwtUtils(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expire-ms:43200000}") long expireTime) {
        this.secretKey = secretKey;
        this.expireTime = expireTime;
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
