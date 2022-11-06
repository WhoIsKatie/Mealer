package com.uottawa.seg2105.group10.backend;

public abstract class User {
	protected String firstName, lastName, email, password, type;


	public User(String firstName, String lastName, String email, String password, String type) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.type = type;
	}

	public User(){}
	public String getFirstName(){return firstName;}
	public String getLastName(){return lastName;};
	protected String getPassword(){return password;}
	protected String getEmail(){return email;}
	public String getType(){return type;}
}