package com.uottawa.seg2105.group10.backend;

public abstract class User {
	protected String firstName, lastName, email, password, address, type;


	public User(String firstName, String lastName, String email, String password, String address, String type) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.type = type;
	}

	public User(){}

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

	public String getAddress() { return address; }

	public String getType() { return type; }

}