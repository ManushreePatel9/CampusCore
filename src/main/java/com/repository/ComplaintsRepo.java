package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.ComplaintsEntity;
import com.entity.UserEntity;

public interface ComplaintsRepo extends JpaRepository<ComplaintsEntity, Integer>{
	List<ComplaintsEntity> findByUserUserId(Integer userId);
}
