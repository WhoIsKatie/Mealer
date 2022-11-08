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
            String cook = documentSnapshot.getString("cookUid");
            assert cook != null;
            dBase.collection("users").document(cook).update("isSuspended", true);
            // WE REMOVE USERMANAGER SO NEW WAY TO GET THE COOK (FROM FB 'USER' FIELD)
            //Cook thisCook = UserManager.getCooks().get(cook);

            // TODO: NEED TO FILL FIREBASE USERS TO HAVE THEIR MEALER 'USER' OBJECT
            Cook thisCook = (Cook) documentSnapshot.get("User");
            assert thisCook != null;
            thisCook.addSuspension(length);
            dBase.collection("users").document(cook).update("suspensionEnd", thisCook.getSuspensionEnd());
        });
        docRef.update("status", false);
    }
    //indefinitely
    public static void suspendCook(DocumentReference docRef){
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            String cook = documentSnapshot.getString("cookUid");
            assert cook != null;
            dBase.collection("users").document(cook).update("isSuspended", true);
            // WE REMOVE USERMANAGER SO NEW WAY TO GET THE COOK (FROM FB 'USER' FIELD)
            //Cook thisCook = UserManager.getCooks().get(cook);

            // TODO: NEED TO FILL FIREBASE USERS TO HAVE THEIR MEALER 'USER' OBJECT
            Cook thisCook = (Cook) documentSnapshot.get("User");
            assert thisCook != null;
            thisCook.addSuspension(null);
                dBase.collection("users").document(cook).update("suspensionEnd", thisCook.getSuspensionEnd());
            });

        docRef.update("status", false);
    }
}
