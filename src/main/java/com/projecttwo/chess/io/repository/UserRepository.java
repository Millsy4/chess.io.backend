package com.projecttwo.chess.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projecttwo.chess.io.model.User;

public interface UserRepository extends JpaRepository<User, String> {
	
}
