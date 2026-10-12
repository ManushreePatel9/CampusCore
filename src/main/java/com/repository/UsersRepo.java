package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.UserEntity;

public interface UsersRepo extends JpaRepository<UserEntity, Integer>{
	Optional<UserEntity> findByEmailAndPassword(String email , String password);
	Optional<UserEntity> findByEmail(String email);
}
