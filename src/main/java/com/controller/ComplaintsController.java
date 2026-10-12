package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.entity.ComplaintsEntity;
import com.entity.UserEntity;
import com.repository.ComplaintsRepo;
import com.repository.UsersRepo;
import com.util.JwtUtil;

import java.util.List;
import java.util.Optional;

@RestController
public class ComplaintsController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ComplaintsRepo complaintsRepo;

	@Autowired
	private UsersRepo userRepo;

	@PostMapping("addComplaints")
	public ResponseEntity<?> addComplaints(@RequestBody ComplaintsEntity complaintsEntity,
			@RequestHeader("Authorization") String authHeader) {

		// 1. Check karo ki header mein "Bearer " se token aa raha hai ya nahi
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(401).body("Invalid Token");
		}

		// 2. "Bearer " hata kar sirf token nikal lo (7 characters ke baad)
		String token = authHeader.substring(7);

		// 3. Token validate karo (Sahi hai ya expired toh nahi?)
		if (!jwtUtil.validateToken(token)) {
			return ResponseEntity.status(401).body("Invalid Token");
		}

		// 4. Token se email (username) nikal lo
		String email = jwtUtil.extractEmail(token);

		// 5. Database se user dhundo taaki complaint ke sath link ho sake
		Optional<UserEntity> opUser = userRepo.findByEmail(email);

		if (opUser.isPresent()) {
			UserEntity user = opUser.get();

			// Complaint ke sath user (Foreign Key) set kar diya
			complaintsEntity.setUser(user);

			// Database mein save kar diya
			complaintsRepo.save(complaintsEntity);

			return ResponseEntity.status(200).body("Complaint added successfully");
		} else {
			return ResponseEntity.status(404).body("User not found");
		}
	}

	@GetMapping("/viewComplaints")
	public ResponseEntity viewComplaints(@RequestHeader String authHeader) {

		// 1. Token check karo
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(401).body("Unauthorized: Token missing or invalid!");
		}

		String token = authHeader.substring(7);

		// 2. Token validate karo
		if (!jwtUtil.validateToken(token)) {
			return ResponseEntity.status(401).body("Unauthorized: Invalid or Expired Token!");
		}

		// 3. Email nikalo
		String email = jwtUtil.extractEmail(token);

		// 4. User dhundo
		Optional<UserEntity> opUser = userRepo.findByEmail(email);

		if (opUser.isPresent()) {
			UserEntity user = opUser.get();
			int userId = user.getUserId();
			// 5. Sirf usi user ki complaints database se fetch karo
			List<ComplaintsEntity> complaints = complaintsRepo.findByUserUserId(userId);

			return ResponseEntity.status(200).body(complaints);
		} else {
			return ResponseEntity.status(401).body("User not found from this token!");
		}

	}

}