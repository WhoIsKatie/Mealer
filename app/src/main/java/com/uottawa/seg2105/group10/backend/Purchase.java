package com.uottawa.seg2105.group10.backend;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.util.Log;

import androidx.annotation.StringDef;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.annotation.Retention;

public class Purchase {

    private final String cookUID, clientUID, mealID, clientName, pickupTime, requestTime;
    private @PurchaseStatus String status;
    private DocumentReference complaint;
    private final FirebaseFirestore dBase = FirebaseFirestore.getInstance();
    private static final String TAG = "Purchase Class";

    @Retention(SOURCE) //https://stackoverflow.com/questions/24715096/how-to-only-allow-certain-values-as-parameter-for-a-method-in-java
    @StringDef({"PENDING", "ACCEPTED", "REJECTED"})
    public @interface PurchaseStatus{}
    public static final String PENDING = "pending";
    public static final String ACCEPTED = "accepted";
    public static final String REJECTED = "rejected";

    public Purchase(String requestTime, String cookUID, String clientUID, String mealName, String pickupTime, String clientName){
        this.clientUID = clientUID;
        this.cookUID = cookUID;
        this.requestTime = requestTime;             // the creation time of this instance
        this.mealID = mealName;                     // the meal name
        this.clientName = clientName;
        complaint = null;
        status = "PENDING";
        this.pickupTime = pickupTime.toString();
        updateFireStore();
    }

    public Purchase(){
        clientUID = "";
        cookUID = "";
        requestTime = "";
        mealID = "";
        clientName = "";
        complaint = null;
        status = "PENDING";
        pickupTime = "";
    }

    //getters
    public String getMealName(){return mealID;}
    public String getMealID(){return mealID;}
    public String getClientName(){return clientName;}
    public String getCookUID() {return cookUID;}
    public String getClientUID() {return clientUID;}
    public DocumentReference getComplaint() {return complaint;}
    public String getStatus(){return status;}
    public String getPickUpTime() {return pickupTime;}
    public String getRequestTime() {return requestTime;}

    //complaint setter (the only one that can be set after creation)
    public boolean setComplaint(DocumentReference complaint){
        this.complaint = complaint;
        return updateFireStore();
    }

    public boolean updateStatus(@PurchaseStatus String status){
        this.status = status;
        return updateFireStore();
    }

    public boolean updateFireStore() { //the purchase itself interacts with firebase so hopefully outside classes don't have to
        final boolean[] flag = new boolean[1];
        //collection purchases => document with ID = cook UID (so cook can easily find their sales) => collection with client UID => new document with object = this purchase instance
        dBase.collection("purchases").document(requestTime).set(this).addOnSuccessListener(v -> {
            Log.d(TAG, "Purchase added successfully");
            flag[0] = true;
        }).addOnFailureListener(e -> {
            Log.d(TAG, "Could not add the purchase");
            flag[0] = false;
        });
        return flag[0];
    }
}
