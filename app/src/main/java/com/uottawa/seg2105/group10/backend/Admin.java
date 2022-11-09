package com.uottawa.seg2105.group10.backend;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Duration;

public class Admin extends User{
    //create singular object of Admin
    private static Admin instance = new Admin("Jacob", "Maurice", "admin@mealer.com", "admin123!", "Admin");
    private static FirebaseFirestore dBase = FirebaseFirestore.getInstance();
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


    public static void dismissComplaint(DocumentReference docRef) {
        docRef.update("status", false);
    }

    public static void suspendCook(DocumentReference docRef, Duration length){
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            String cookid = documentSnapshot.getString("cookUid");
            assert cookid != null;
            DocumentReference userDoc = dBase.collection("users").document(cookid);
            userDoc.update("isSuspended", true);

            userDoc.collection("userObject").document("Cook").get().addOnSuccessListener(snapshot -> {
                Cook thisCook = snapshot.toObject(Cook.class);;
                System.out.println(thisCook != null);
                thisCook.addSuspension(length);
                userDoc.collection("userObject").document("Cook").set(thisCook);
            });
        });
        dismissComplaint(docRef);
    }
}
