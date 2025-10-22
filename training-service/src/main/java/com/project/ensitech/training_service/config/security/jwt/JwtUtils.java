package com.project.ensitech.training_service.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.List;

@Service
public class JwtUtils {

    // @Value("${jwt.secret-key}")
    private static  String SECRET_KEY= "6cf511a59725fab8400f66c29f7ce41a398116b16914a538e1dcc389e07ee3fb";


    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("err"+ e);
            return false; // invalid, expired, or tampered token
        }
    }


    private static Key getSigningKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public static String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public static List<GrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        String role = claims.get("role", String.class); // récupère la clé unique "role"

        if (role != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return Collections.emptyList();
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
