package com.projecttwo.chess.io.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(unique = true)
	private String username;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "wins")
	private int wins;
	
	@Column(name = "losses")
	private int losses;
	
	@Column(name = "ties")
	private int ties;
	
	@Column(name = "photoLoc")
	private String photoLoc;
	
}
