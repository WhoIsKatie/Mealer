package com.uottawa.seg2105.group10.backend;

import com.google.firebase.firestore.DocumentReference;

import java.time.Duration;

public class Admin extends User{
    //create singular object of Admin
    private static Admin instance = new Admin("Jacob", "Maurice", "admin@mealer.com", "admin123!", "Admin");

    /*
    private static String [] inbox; //Complaints from Clients
    private static String [] suspendComplaints;
    private static String [] acceptComplaints;
    */

    //class cannot be instantiated
    private Admin(String firstName, String lastName, String email, String password, String type) {
        super(firstName, lastName, email, password, type);
    }

    // Returns the only UserManager instance available
    public static Admin getInstance(){ return instance; }

    public String getEmail() {return email;}
    public String getPassword() {return password;}


    public static void dismissComplaint(DocumentReference docRef) {}

    // Also - if a cook has already been suspended (from a previous complaint),
    // just extend the duration >:)
    // temporarily
    public static void suspendCook(DocumentReference docRef, Duration length){}
    //indefinitely
    public static void suspendCook(DocumentReference docRef){}
}
