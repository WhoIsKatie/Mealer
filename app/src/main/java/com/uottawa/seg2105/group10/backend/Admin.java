package com.uottawa.seg2105.group10.backend;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    String cook = document.getString("cookUid");
                    dBase.collection("users").document(cook).update("isSuspended", true);
                    Cook thisCook = UserManager.getCooks().get(cook);
                    thisCook.addSuspension(length);
                    thisCook.getSuspensionEnd();
                    dBase.collection("users").document(cook).update("suspensionEnd", thisCook.getSuspensionEnd());
                }
            }
        });
        docRef.update("status", false);
    }
    //indefinitely
    public static void suspendCook(DocumentReference docRef){

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    String cook = document.getString("cookUid");
                    dBase.collection("users").document(cook).update("isSuspended", true);
                    Cook thisCook = UserManager.getCooks().get(cook);
                    thisCook.addSuspension(null);
                    thisCook.getSuspensionEnd();
                    dBase.collection("users").document(cook).update("suspensionEnd", thisCook.getSuspensionEnd());
                }
            }
        });
        docRef.update("status", false);
    }
}
