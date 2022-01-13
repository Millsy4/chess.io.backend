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

	@PostMapping("/showall")
	public ResponseEntity<List<User>> getAllUsers(@RequestBody User user) {
		Optional<User> userData = userRepository.findById(user.getUsername());
		
		if (userData.isPresent()) {
			User _user_ = userData.get();
			if (_user_.getPassword().equals(user.getPassword())) {
				List<User> users = new ArrayList<User>();
				
				List<User> _users = userRepository.findAll();
				
				for (int i = 0; i < _users.size(); i++) {
					if (_users.get(i).getUsername().equals("knights")) {
						System.out.println("Admin user removed from data");
					} else {
						User tempUser = new User(_users.get(i).getUsername(), _users.get(i).getName(), _users.get(i).getPhotoLoc());
						users.add(tempUser);
					}
				};
				
				if (users.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				} else {
					return new ResponseEntity<>(users, HttpStatus.OK);
				}
				
			} else {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
		}
	}

	@GetMapping("/users/{username}")
	public String getUserById(@PathVariable("username") String username) {

		Optional<User> userData = userRepository.findById(username);
			if (userData.isPresent()) {
				return "Username exists";
			} else {
				return "Username is available";
			}
	}

	@PostMapping("/users/login")
	public ResponseEntity<User> getUserLogin(@RequestBody User user) {
		Optional<User> userData = userRepository.findById(user.getUsername());
		if (userData.isPresent()) {
			User _user = userData.get();
			if (_user.getPassword().equals(user.getPassword())) {
				boolean isUserInGame = _user.isInGame();
				
				if (isUserInGame == true) {
					_user.setInGame(false);
					
					double randomizer = (Math.random() * 1000000);
					int randomCode = (int) (randomizer);
					_user.setLoginCode(randomCode);
					
					return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK); // 200
				} else {
					double randomizer = (Math.random() * 1000000);
					int randomCode = (int) (randomizer);
					_user.setLoginCode(randomCode);
					
					return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK); // 200
				}
			} else {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
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
						user.getEmail(), 0, 0, 0, "https://s3.us-east-2.amazonaws.com/chessio.images/4850728.jpg", 0, false));
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
	
	@PostMapping("/users/wins/{username}")
	public ResponseEntity<User> updateUserWins(@PathVariable("username") String username) {
		Optional<User> userData = userRepository.findById(username);
		if (userData.isPresent()) {
			User user = userData.get();
			user.setWins(user.getWins() + 1);
			return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/users/losses/{username}")
	public ResponseEntity<User> updateUserLosses(@PathVariable("username") String username) {
		Optional<User> userData = userRepository.findById(username);
		
		if (userData.isPresent()) {
			User user = userData.get();
			user.setLosses(user.getLosses() + 1);
			return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/users/ties/{username}")
	public ResponseEntity<User> updateUserTies(@PathVariable("username") String username) {
		Optional<User> userData = userRepository.findById(username);
		if (userData.isPresent()) {
			System.out.println(userData);
			User user = userData.get();
			System.out.println(user);
			user.setTies(user.getTies() + 1);
			System.out.println(user);
			return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/users/ingame/{username}")
	public ResponseEntity<User> updateUserInGame(@PathVariable("username") String username) {
		Optional<User> userData = userRepository.findById(username);
		if (userData.isPresent()) {
			User user = userData.get();
			boolean isUserInGame = user.isInGame();
			if (isUserInGame == false) {
				user.setInGame(true);
				return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
			} else {
				user.setInGame(false);
				return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
//	@DeleteMapping("/users/{username}")
//	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("username") String username) {
//		try {
//			userRepository.deleteById(username);
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
}
