package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Purchase;
import com.uottawa.seg2105.group10.recyclers.Purchase_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.recyclers.RecyclerViewInterface;
import com.uottawa.seg2105.group10.ui.clientView.MealSearch;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class ClientHome extends AppCompatActivity implements RecyclerViewInterface {

    //initializing variables or instances
    protected RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private DocumentSnapshot document2, document3;
    private static final String TAG = "Client Home";
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText cookName, complaint, cookName2, rate, titleComplaint;
    private TextView rateTheCook, explain, requestTime, purchasedName, purchasedCook, purchasedPrice, clientPickupTime,purchaseStatus, clientName2;
    private Button submitButton, cancelButton, complain, rateCook, submitButton2, cancelButton2, searchButton;
    private DocumentReference clientRef, cookRef, complaintRef, userRef;
    private CollectionReference purchaseRef;
    private ArrayList<Purchase> purchasesArrayList;
    private Purchase_RecyclerViewAdapter purchasesRVAdapter;
    String clientName, cookUID2, mealID, status, mealName, pickUpTime2, request_Time, clientUID2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienthome);

        // Initialize Firebase Authority and Firebase Firestore objects
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        String userName = mAuth.getCurrentUser().getUid();
        userRef = dBase.collection("users").document(userName);
        purchaseRef = dBase.collection("purchases");

        recyclerView = findViewById(R.id.clientRecyclerView);

        //initializing textviews
        /*requestTime = findViewById(R.id.requestTime);
        purchasedName = findViewById(R.id.purchasedName);
        purchasedCook = findViewById(R.id.purchasedCook);
        purchasedPrice = findViewById(R.id.purchasedPrice);
        clientPickupTime = findViewById(R.id.clientPickupTime);
        purchaseStatus = findViewById(R.id.purchaseStatus);
        clientName2 = findViewById(R.id.clientName);*/
        /*complain = (Button)findViewById(R.id.complain);
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComplaint();
            }
        });

        rateCook = (Button)findViewById(R.id.rateCook);
        rateCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add rate your cook method here
                rateYourCook();
            }
        });*/

        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClientHome.this, MealSearch.class));
            }

        });

        /*purchaseRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    long mostRecent = 0;
                    for(DocumentSnapshot document2 : task.getResult()){
                        Log.d(TAG, "DocumentSnapshot data: " + document2.getData());
                        clientName = document2.getString("clientName");
                        clientUID2 = document2.getString("clientUID");
                        cookUID2 = document2.getString("cookUID");
                        mealID = document2.getString("mealID");
                        status = document2.getString("status");
                        mealName = document2.getString("mealName");
                        pickUpTime2 = document2.getString("pickUpTime");
                        request_Time = document2.getString("requestTime");
                        if(userName.equals(document2.getString("clientUID")) && Long.parseLong(document2.getString("requestTime"))>mostRecent){

                            mostRecent = Long.parseLong(document2.getString("requestTime"));
                        }

                    }
                    updateClientHome();
                }
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpPurchaseModels();
    }

    private void setUpPurchaseModels(){
        // initializing all lists of fields
        ArrayList<String> cookUID = new ArrayList<>();
        ArrayList<String> clientUID = new ArrayList<>();
        ArrayList<String> mealID = new ArrayList<>();
        ArrayList<String> imageID = new ArrayList<>();
        ArrayList<String> clientName = new ArrayList<>();
        ArrayList<String>  pickupTime = new ArrayList<>();
        ArrayList<String>  requestTime = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();
        purchasesArrayList = new ArrayList<>();
        //TODO: you can also decide whether to forgo fetching the meal image to display

        purchaseRef.whereEqualTo("clientUID", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for(QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + "=>" + document.getData());
                    Map<String, Object> data = document.getData();
                    if(!Objects.equals(data.get("cookUID"), null))
                        cookUID.add((String) data.get("cookUID"));
                    else cookUID.add(null);

                    if(!Objects.equals(data.get("clientUID"), null))
                        clientUID.add((String) data.get("clientUID"));
                    else clientUID.add(null);

                    if(!Objects.equals(data.get("mealID"), null))
                        mealID.add((String) data.get("mealID"));
                    else mealID.add(null);

                    if(!Objects.equals(data.get("clientName"), null))
                        clientName.add((String) data.get("clientName"));
                    else clientName.add(null);

                    if(!Objects.equals(data.get("pickUpTime"), null))
                        pickupTime.add((String) data.get("pickUpTime"));
                    else pickupTime.add(null);

                    if(!Objects.equals(data.get("requestTime"), null))
                        requestTime.add((String) data.get("requestTime"));
                    else requestTime.add(null);
                    if(!Objects.equals(data.get("imageID"), null))
                        imageID.add((String) data.get("imageID"));
                    else imageID.add(null);
                    if(!Objects.equals(data.get("status"), null))
                        status.add((String) data.get("imageID"));
                    else status.add(null);
                }
                for (int i = 0; i < clientUID.size(); i++){
                    Purchase purchase = new Purchase(requestTime.get(i), cookUID.get(i), clientUID.get(i), mealID.get(i), imageID.get(i), pickupTime.get(i), clientName.get(i), status.get(i));
                    purchasesArrayList.add(purchase);
                }
                updateClientHome();
            }

        });
    }

    private void updateClientHome(){
        /*clientName2.setText(clientName);
        requestTime.setText(request_Time);
        purchasedName.setText(mealName);
        purchasedCook.setText(cookUID2);
       // purchasedPrice.setText(--);
        clientPickupTime.setText(pickUpTime2);
        purchaseStatus.setText(status);
        switch (status) {
            case "PENDING":
            case "REJECTED":
                complain.setVisibility(View.GONE);
                rateCook.setVisibility(View.GONE);
                break;
            case" ACCEPTED":
                complain.setVisibility(View.VISIBLE);
                rateCook.setVisibility(View.VISIBLE);
                break;
        }*/

        purchasesRVAdapter = new Purchase_RecyclerViewAdapter("Client", this, purchasesArrayList, this);
        recyclerView.setAdapter(purchasesRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    /*public void submitComplaint(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View complaintPopup = getLayoutInflater().inflate(R.layout.activity_complaintpopup, null);

        titleComplaint = (EditText) complaintPopup.findViewById(R.id.titleComplaint);
        String titleComplaintString = titleComplaint.getText().toString();

        cookName = (EditText) complaintPopup.findViewById(R.id.cookName);
        String cookNameString = cookName.getText().toString();

        complaint = (EditText) complaintPopup.findViewById(R.id.complaint);
        String complaintString = complaint.getText().toString();

        submitButton =(Button) complaintPopup.findViewById(R.id.submitButton);
        cancelButton = (Button) complaintPopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(complaintPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComplaintModel complaint = new ComplaintModel(clientName, cookNameString, String.valueOf(LocalTime.now()),titleComplaintString, complaintString, cookUID2, clientUID2);
                dBase.collection("complaints").add(complaint);
                dBase.collection("complaints")
                        .add(complaint)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }});

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }*/

    /*public void rateYourCook(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View ratePopup = getLayoutInflater().inflate(R.layout.activity_ratecook, null);

        rateTheCook = (TextView) ratePopup.findViewById(R.id.rateTheCook);
        explain = (TextView) ratePopup.findViewById(R.id.explain);

        cookName2 = (EditText) ratePopup.findViewById(R.id.cookName2);
        String cookName2String = cookName2.getText().toString();

        rate = (EditText) ratePopup.findViewById(R.id.rate);
        String rateString = rate.getText().toString();
        int rateNum = Integer.parseInt(rateString);


        submitButton =(Button) ratePopup.findViewById(R.id.submitButton2);
        cancelButton = (Button) ratePopup.findViewById(R.id.cancelButton2);

        dialogBuilder.setView(ratePopup);
        dialog = dialogBuilder.create();
        dialog.show();

        submitButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // collect user input from text fields, call addRating() on cook object, update cook.
                cookRef = dBase.collection("users").document(cookUID2);
                cookRef
                        .update("ratingSum", Integer.parseInt(rateString))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });

            }
        });

        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }*/

    // Should not be clickable
    @Override
    public void onItemClick(int position) {}
}
