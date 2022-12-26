package com.uottawa.seg2105.group10.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class User {
	protected String firstName, lastName, email, password, type, uid;
	protected final FirebaseFirestore dBase = FirebaseFirestore.getInstance();

	public User(String firstName, String lastName, String email, String password, String type, String uid) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.type = type;
		this.uid = uid;
	}

	/** Constructor for Firebase access.
	 *  Do not use locally unless you want an empty user.
	 */
	public User(){}
	public String getFirstName(){return firstName;}
	public String getLastName(){return lastName;};
	public String getPassword(){return password;}
	public String getEmail(){return email;}
	public String getType(){return type;}
	public String getUid(){return uid;}

	//the user itself interacts with firebase so hopefully outside classes won't have to anymore
	public boolean updateFireStore() {
		final boolean[] flag = new boolean[1];
		//collection users => document with ID = UID => collection with userObject => new document with object = this user instance
		String tag = type + " Class";
		dBase.collection("users").document(uid).set(this).addOnSuccessListener(v -> {
			Log.d(tag, "User updated successfully");
			flag[0] = true;
		}).addOnFailureListener(e -> {
			Log.d(tag, "Could not add the user to Firebase DB");
			flag[0] = false;
		});
		return flag[0];
	}

}