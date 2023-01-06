package com.uottawa.seg2105.group10.repositories;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.util.Log;

import androidx.annotation.StringDef;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.annotation.Retention;
import java.util.Locale;
import java.util.Objects;

public class Purchase {

    private final String cookUID, clientUID, mealID, cookName, pickupTime, requestTime, clientName;
    private String imageID = null;
    private @PurchaseStatus String status;
    private boolean complained, rated;
    private FirebaseFirestore dBase = FirebaseFirestore.getInstance();
    private static final String TAG = "Purchase Class";

    @Retention(SOURCE) //https://stackoverflow.com/questions/24715096/how-to-only-allow-certain-values-as-parameter-for-a-method-in-java
    @StringDef({"PENDING", "ACCEPTED", "REJECTED"})
    public @interface PurchaseStatus{}
    public static final String PENDING = "pending";
    public static final String ACCEPTED = "accepted";
    public static final String REJECTED = "rejected";

    public Purchase(String requestTime, String cookUID, String clientUID, String mealName, String imageID, String pickupTime, String cookName, String clientName, String status, boolean rated, boolean complained){
        this.clientUID = clientUID;
        this.cookUID = cookUID;
        this.requestTime = requestTime;             // the creation time of this instance
        this.mealID = mealName;                     // the meal name
        this.cookName = cookName;
        this.imageID = imageID;
        this.clientName = clientName;
        this.status = status;
        this.pickupTime = pickupTime;
        this.rated = rated;
        this.complained = complained;
        if(!Objects.equals(cookName, "")) {
            dBase = FirebaseFirestore.getInstance();
            updateFireStore();
        }
    }


    /** Constructor for Firebase access.
     *  Do NOT use locally unless you want an empty purchase.
     */
    public Purchase(){
        clientUID = "";
        cookUID = "";
        requestTime = "";
        mealID = "";
        cookName = "";
        clientName = "";
        status = "PENDING";
        pickupTime = "";
    }

    //getters
    public String getMealID(){return mealID;}
    public String getCookName(){return cookName;}
    public String getCookUID() {return cookUID;}
    public String getClientUID() {return clientUID;}
    public boolean isRated() {return rated;}
    public boolean isComplained() {return complained;}
    public String getClientName(){return clientName;}
    public String getStatus(){return status;}
    public String getPickupTime() {return pickupTime;}
    public String getRequestTime() {return requestTime;}
    public String getImageID(){return imageID;}

    public boolean setComplained(){
        if (!(status.equals(ACCEPTED.toUpperCase(Locale.ROOT)))) return false;
        this.complained = true;
        return updateFireStore();
    }

    public boolean setRated(){
        if (!(status.equals(ACCEPTED.toUpperCase(Locale.ROOT)))) return false;
        this.rated = true;
        return updateFireStore();
    }

    public boolean setImageID(String imageID){
        this.imageID = imageID;
        return updateFireStore();
    }

    public boolean updateStatus(String status){
        this.status = status;
        return updateFireStore();
    }

    public boolean updateFireStore() {
        final boolean[] flag = new boolean[1];
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