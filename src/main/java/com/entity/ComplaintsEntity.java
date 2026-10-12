package com.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "complaints")
public class ComplaintsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer complaintId;

	@ManyToOne
	@JoinColumn(name = "userId")
	UserEntity user;

	String complaintDetails;
	
	String complaintCategory;
	String status;

	LocalDateTime createdAt;
	

	@PrePersist
	public void f() {
		this.createdAt = LocalDateTime.now();
	}

}
