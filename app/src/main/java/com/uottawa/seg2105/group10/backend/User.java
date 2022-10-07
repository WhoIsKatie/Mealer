package com.uottawa.seg2105.group10.backend;

import com.google.firebase.auth.FirebaseUser;

public abstract class User {
	protected String firstName, lastName, email, password, address;
	protected FirebaseUser user;

	public User(FirebaseUser user, String firstName, String lastName, String email, String password, String address) {
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getAddress() {
		return address;
	}

}