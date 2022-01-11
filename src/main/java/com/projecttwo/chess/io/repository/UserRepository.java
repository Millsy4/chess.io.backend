package com.projecttwo.chess.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projecttwo.chess.io.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
}
