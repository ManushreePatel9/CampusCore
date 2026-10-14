package com.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "mysecretkey123333333333333333333333333333333333333";

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

   
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)                                   
                .claim("userRole", role)                             
                .setIssuedAt(new Date())                            
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiry time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)       
                .compact();
    }

  
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

 
    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("userRole", String.class);
    }


    public boolean isTokenExpired(String token) {
        Date expiryDate = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiryDate.before(new Date()); 
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}