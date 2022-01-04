package com.projecttwo.chess.io.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projecttwo.chess.io.model.User;
import com.projecttwo.chess.io.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> users = new ArrayList<User>();

			userRepository.findAll().forEach(users::add);

			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/users/{username}")
	public ResponseEntity<User> getUserById(@PathVariable("username") String username) {
		Optional<User> userData = userRepository.findById(username);

		if (userData.isPresent()) {
			return new ResponseEntity<>(userData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try {
			Optional<User> userData = userRepository.findById(user.getUsername());
			
			if (userData.isPresent()) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				User _user = userRepository
						.save(new User(user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), 0, 0, 0, "https://s3.us-east-2.amazonaws.com/chessio.images/4850728.jpg"));
				return new ResponseEntity<>(_user, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/users/{username}")
	public ResponseEntity<User> updateUser(@PathVariable("username") String username, @RequestBody User user) {
		Optional<User> userData = userRepository.findById(username);

		if (userData.isPresent()) {
			User _user = userData.get();
			_user.setUsername(user.getUsername());
			_user.setPassword(user.getPassword());
			_user.setName(user.getName());
			_user.setWins(user.getWins());
			_user.setLosses(user.getLosses());
			_user.setTies(user.getTies());
			_user.setPhotoLoc(user.getPhotoLoc());
			return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@DeleteMapping("/users/{username}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("username") String username) {
		try {
			userRepository.deleteById(username);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
