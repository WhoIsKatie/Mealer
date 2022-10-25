package com.uottawa.seg2105.group10.backend;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class UserManager {
    private static HashMap<FirebaseUser, User> instance = new HashMap<>(); //only instance ever
    // used singleton pattern from https://en.wikipedia.org/?title=Singleton_pattern#Implementations

    private UserManager(){} //cannot make instances

    public static HashMap<FirebaseUser, User> getUsers() { //static so can access anywhere
        return instance;
    }


}
