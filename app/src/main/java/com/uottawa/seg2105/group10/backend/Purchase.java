package com.uottawa.seg2105.group10.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Purchase {

    private final String cookUID, clientUID, mealID, date;  //just keep date like August 21 2020, not actual 'Date' structure
    private DocumentReference complaint;
    private FirebaseFirestore dBase = FirebaseFirestore.getInstance();
    private static final String TAG = "Purchase Class";

    //should we keep a connection to complaints or just let complaint see us?
    // method to make a purchase inside meal? or just make new document in fb?

    public Purchase(String cookUID, String clientUID, String mealID, String date){
        this.clientUID = clientUID;
        this.cookUID = cookUID;
        this.mealID = mealID;
        this.date = date;
        complaint = null;
        updateFireStore();
    }

    //getters
    public String getCookUID() {return cookUID;}
    public String getClientUID() {return clientUID;}
    public String getMealID() {return mealID;}
    public String getDate() {return date;}
    public DocumentReference getComplaint() {return complaint;}

    //complaint setter (the only one that can be set after creation)
    public void setComplaint(DocumentReference complaint){
        this.complaint = complaint;
        updateFireStore();
    }

    public boolean updateFireStore(){
        final boolean[] flag = new boolean[1];
        dBase.collection("purchases").document(cookUID).collection(mealID).add(this).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Purchase added successfully");
                    flag[0] = true;
                }
                else{
                    Log.d(TAG, "Could not add the purchase");
                    flag[0] = false;
                }
            }
        });
        return flag[0];
    }

}
