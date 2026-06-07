package com.example.admin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private static final String USER_ID_CLAIM = "userId";
    private static final String PERMISSIONS_CLAIM = "permissions";

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(Long userId, String username, List<String> permissions) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(username)
                .claim(USER_ID_CLAIM, userId)
                .claim(PERMISSIONS_CLAIM, permissions)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey())
                .compact();
    }

    public Long getUserId(String token) {
        return parseClaims(token).get(USER_ID_CLAIM, Long.class);
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public List<String> getPermissions(String token) {
        Object permissions = parseClaims(token).get(PERMISSIONS_CLAIM);
        if (!(permissions instanceof List<?> permissionList)) {
            return List.of();
        }

        return permissionList.stream()
                .map(String::valueOf)
                .toList();
    }

    public boolean isTokenValid(String token) {
        parseClaims(token);
        return true;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
