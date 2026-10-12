package com.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Persistent;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	int userId;
	
	Integer enrollment;
	String fullName;
	String email;
	String password;
	String department;
	String branch;
	String role = "STUDENT";
	LocalDateTime createdAt;
	
	@PrePersist
	public void f() {
		this.createdAt = LocalDateTime.now(); 
	}
	
	
}
