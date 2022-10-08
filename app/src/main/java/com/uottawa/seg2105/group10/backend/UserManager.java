package com.uottawa.seg2105.group10.backend;

import java.util.HashMap;

public class UserManager {
    // TODO: replace this mf with Firebase storage >:(

    // TEMPORARY collection of users >:(
    private HashMap<String, User> users;

    public UserManager() {
        users = new HashMap<String, User>();
    }

    public boolean addUser(User newUser) {
        if (newUser != null) {
            users.put(newUser.getEmail(), newUser);
        }
        return false;
    }

    public User getUser(String email) {
        return users.get(email);
    }

    // TEMPORARY I SAY!!!!!
}
