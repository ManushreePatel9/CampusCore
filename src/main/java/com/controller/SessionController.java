package com.controller;

//import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.CampusCoreApplication;
import com.entity.LoginEntity;
import com.entity.UserEntity;
import com.repository.UsersRepo;
import com.util.JwtUtil;

@RestController
public class SessionController {

	private final CampusCoreApplication campusCoreApplication;

	@Autowired
	UsersRepo userRepo;
	
	@Autowired
	JwtUtil jwtUtil;

	SessionController(CampusCoreApplication campusCoreApplication) {
		this.campusCoreApplication = campusCoreApplication;
	}

	@PostMapping("/signup")
	public String signup(@RequestBody UserEntity user) {
		userRepo.save(user);
		return "Signup Successful";
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginEntity loginEntity) {

		Optional<UserEntity> op = userRepo.findByEmailAndPassword(loginEntity.getEmail(), 
				loginEntity.getPassword());
		
		if (op.isPresent()) {
			UserEntity user = op.get();
			String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
			
			return ResponseEntity.status(200).body("Login Successfull with token "+token);
		}else {
			return ResponseEntity.status(401).body("User not found");
		}

	}
}
