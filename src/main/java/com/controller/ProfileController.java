package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.entity.UserEntity;
import com.repository.UsersRepo; // Agar aapne UserRepo banaya hai toh apna package/naam likhein
import com.util.JwtUtil;

import java.util.Optional;

@RestController
public class ProfileController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersRepo userRepo; // (Agar aapka naam userRepo hai toh yahan change kar lena)

    @GetMapping("viewProfile")
    public ResponseEntity<?> viewProfile(@RequestHeader("Authorization") String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized: Token missing or invalid!");
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid or Expired Token!");
        }

        String email = jwtUtil.extractEmail(token);

        Optional<UserEntity> opUser = userRepo.findByEmail(email);

        if (opUser.isPresent()) {
            UserEntity user = opUser.get();
            return ResponseEntity.status(200).body(user);
        } else {
            return ResponseEntity.status(404).body("User not found!");
        }
    }
    
    @PutMapping("updateProfile")
    public ResponseEntity<String> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserEntity updatedDetails) {
        
        // 1. Token check karo
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized: Token missing!");
        }

        String token = authHeader.substring(7);

        // 2. Token validate karo
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid or Expired Token!");
        }

        // 3. Token se email nikalo
        String email = jwtUtil.extractEmail(token);

        // 4. Database se purana user dhundo
        Optional<UserEntity> opUser = userRepo.findByEmail(email);

        if (opUser.isPresent()) {
            UserEntity existingUser = opUser.get();
            
            if (updatedDetails.getFullName() != null) {
                existingUser.setFullName(updatedDetails.getFullName());
            }
            
            if (updatedDetails.getDepartment() != null) {
                existingUser.setDepartment(updatedDetails.getDepartment());
            }
            
            if (updatedDetails.getBranch() != null) {
                existingUser.setBranch(updatedDetails.getBranch());
            }
            // 6. Database mein wapas save/update kar do
            userRepo.save(existingUser);

            return ResponseEntity.status(200).body("Profile Updated Successfully!");
        } else {
            return ResponseEntity.status(404).body("User not found!");
        }
    }
    
}