package com.uottawa.seg2105.group10.backend;

import java.util.HashMap;

public class UserManager {
    //create singular object of UserManager
    //private static UserManager instance = new UserManager();

    private static HashMap<String, Cook> cooks = new HashMap<>(); //only instance ever
    private static HashMap<String, Client> clients = new HashMap<>();
    // used singleton pattern from https://en.wikipedia.org/?title=Singleton_pattern#Implementations

    //class cannot be instantiated
    private UserManager(){
    }

    // Returns the only UserManager instance available
    //public static UserManager getInstance(){ return instance; }

    public static HashMap<String, Cook> getCooks() { //static so can access anywhere
        return cooks;
    }

    public static HashMap<String, Client> getClients(){
        return clients;
    }

}
