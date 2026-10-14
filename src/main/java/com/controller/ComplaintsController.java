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

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(401).body("Invalid Token");
		}

		String token = authHeader.substring(7);

		if (!jwtUtil.validateToken(token)) {
			return ResponseEntity.status(401).body("Invalid Token");
		}

		String email = jwtUtil.extractEmail(token);

		Optional<UserEntity> opUser = userRepo.findByEmail(email);

		if (opUser.isPresent()) {
			UserEntity user = opUser.get();
			complaintsEntity.setUser(user);

		
			complaintsRepo.save(complaintsEntity);

			return ResponseEntity.status(200).body("Complaint added successfully");
		} else {
			return ResponseEntity.status(404).body("User not found");
		}
	}

	@GetMapping("/viewComplaints")
	public ResponseEntity viewComplaints(@RequestHeader String authHeader) {

	
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
			int userId = user.getUserId();
			List<ComplaintsEntity> complaints = complaintsRepo.findByUserUserId(userId);

			return ResponseEntity.status(200).body(complaints);
		} else {
			return ResponseEntity.status(401).body("User not found from this token!");
		}

	}

}