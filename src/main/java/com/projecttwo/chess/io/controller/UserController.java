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
			
			// username
			// name
			// profile picutre
			
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
		User _user = userData.get();
		if (_user.getLoginCode() != 0) {
			if (userData.isPresent()) {
				return new ResponseEntity<>(userData.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/users/login")
	public ResponseEntity<User> getUserLogin(@RequestBody User user) {
		Optional<User> userData = userRepository.findById(user.getUsername());
		User _user = userData.get();
		if (_user.getPassword().equals(user.getPassword())) {
			double randomizer = (Math.random() * 1000000);
			int randomCode = (int) (randomizer);
			_user.setLoginCode(randomCode);
			return new ResponseEntity<>(_user, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try {
			Optional<User> userData = userRepository.findById(user.getUsername());

			if (userData.isPresent()) {
				return new ResponseEntity<>(null, HttpStatus.I_AM_A_TEAPOT);
			} else {
				User _user = userRepository.save(new User(user.getUsername(), user.getName(), user.getPassword(),
						user.getEmail(), 0, 0, 0, "https://s3.us-east-2.amazonaws.com/chessio.images/4850728.jpg", 0));
				return new ResponseEntity<>(_user, HttpStatus.CREATED);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/users/photoloc/{username}")
	public ResponseEntity<User> updateUser(@PathVariable("username") String username, @RequestBody User user) {
		Optional<User> userData = userRepository.findById(username);

		if (userData.isPresent()) {
			User _user = userData.get();
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
