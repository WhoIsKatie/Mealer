package com.uottawa.seg2105.group10.backend;

import com.google.firebase.auth.FirebaseUser;


public class Admin{
    private String type;

    String [] inbox; //Complaints from Clients
    String [] suspendComplaints;
    String [] acceptComplaints;


    ///matching super class
    public Admin() {
        String email = "jacobmaurice2003@gmail.com";
        String password = "admin123";
        String type ="Admin";

    }
}
