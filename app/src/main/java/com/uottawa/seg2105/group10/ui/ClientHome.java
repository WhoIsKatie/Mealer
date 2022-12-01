package com.uottawa.seg2105.group10.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.recyclers.ComplaintModel;
import com.uottawa.seg2105.group10.recyclers.RecyclerViewInterface;

import java.time.LocalTime;


public class ClientHome extends AppCompatActivity implements RecyclerViewInterface {

    //initializing variables or instances
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "Client Home";
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText cookName, complaint, cookName2, rate, titleComplaint;
    private TextView rateTheCook, explain;
    private Button submitButton, cancelButton, complain, rateCook, submitButton2, cancelButton2;
    private DocumentReference clientRef, cookRef, complaintRef;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienthome);


        // Initialize Firebase Authority and Firebase Firestore objects
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();


        //TODO: query for most recent purchase with field clientUID and display in card

        rateCook = (Button)findViewById(R.id.rateCook);
        complain = (Button)findViewById(R.id.complain);


        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComplaint();
            }
        });
        rateCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add rate your cook method here
                rateYourCook();
            }
        });


    }

    @Override
    public void onItemClick(int position) {
        //TODO: send client to mealview
        return;

    }

    public void submitComplaint(){
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

                //mAuth.getCurrentUser().getUid()
               // clientRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
               // firebaseMeal = dBase.collection("meals").document(name);

                ComplaintModel complaint = new ComplaintModel(null, cookNameString, String.valueOf(LocalTime.now()),titleComplaintString, complaintString, null, null);
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
    }

    public void rateYourCook(){
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
                //TODO: submit button.
                // collect user input from text fields, call addRating() on cook object, update cook.

            }
        });

        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
