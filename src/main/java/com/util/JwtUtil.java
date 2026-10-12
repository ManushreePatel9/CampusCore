package com.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // 🔑 Secret key (Token ko lock/secure karne ke liye)
    private final String SECRET_KEY = "mysecretkey123333333333333333333333333333333333333";

    // ⏰ Token validity (Yahan 10 hours set kiya hai)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    // ===============================
    // 1. Generate JWT Token (Email & Role ke sath)
    // ===============================
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)                                    // Email daal diya (Subject)
                .claim("userRole", role)                              // 👈 Role bhi token ke andar daal diya
                .setIssuedAt(new Date())                              // Token kab bana
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiry time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)       // Secret key se sign/lock kar diya
                .compact();
    }

    // ===============================
    // 2. Extract Username (Token se Email nikalne ke liye)
    // ===============================
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ===============================
    // 3. Extract Role (Token se Role nikalne ke liye)
    // ===============================
    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("userRole", String.class);
    }

    // ===============================
    // 4. Check Token Expiry (Time khatam toh nahi hua?)
    // ===============================
    public boolean isTokenExpired(String token) {
        Date expiryDate = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiryDate.before(new Date()); // Agar expiry date purani ho gayi toh true dega
    }

    // ===============================
    // 5. Validate Token (Sahi hai ya fake/expired?)
    // ===============================
    public boolean validateToken(String token) {
        try {
            // Agar token decode ho gaya aur koi exception nahi aayi, matlab token valid hai
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Agar token galat ya expired hua toh error aayegi aur ye false return kar dega
            return false;
        }
    }
}