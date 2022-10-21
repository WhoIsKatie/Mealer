package com.uottawa.seg2105.group10.backend;

public class Admin{
    private String type;

    String [] inbox; //Complaints from Clients
    String [] suspendComplaints;
    String [] acceptComplaints;

    public Admin() {
        String email = "admin@mealer.com";
        String password = "admin123!";
        String type ="Admin";

    }
}
