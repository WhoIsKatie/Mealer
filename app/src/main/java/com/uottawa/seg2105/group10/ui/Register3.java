package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Client;
import com.uottawa.seg2105.group10.backend.User;

import java.util.HashMap;
import java.util.Map;

public class Register3 extends AppCompatActivity {

    private static final String TAG = "Register3";

    //Initializing buttons
    private Button nextButt;
    private Button login;
    private ImageButton back;
    private TextInputEditText nameOnCard, CardNumber, ExpDate, cvcField;

    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private User user;

    TextInputLayout nameOnCardLayout, cardNumberLayout, expDateLayout, cvcLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        nextButt = findViewById(R.id.clientSubmitButt);
        login = findViewById(R.id.reg3LoginButt);
        back = findViewById(R.id.reg3BackButt);

        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        nameOnCardLayout = findViewById(R.id.NameLayout);
        cardNumberLayout = findViewById(R.id.ccNumLayout);
        expDateLayout = findViewById(R.id.expiryLayout);
        cvcLayout = findViewById(R.id.cvcLayout);

        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameOnCard = (TextInputEditText) findViewById(R.id.ccNameEditText);
                String fullName = nameOnCard.getText().toString();

                CardNumber = (TextInputEditText) findViewById(R.id.ccNumEditText);
                String ccNum = CardNumber.getText().toString();

                ExpDate = (TextInputEditText) findViewById(R.id.expiryEditText);
                String expiry = ExpDate.getText().toString();

                cvcField = (TextInputEditText) findViewById(R.id.cvcEditText);
                String cvc = cvcField.getText().toString();

                if(!validateNameOnCard() || !validateCardNumber() || !validateExpDate() || !validateCvc()) {
                    return;
                }

                // Add user document with Uid set as document ID to collection of "users" in Firestore
                DocumentReference userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                // Update CC fields
                Map<String, String> data = new HashMap<>();
                data.put("nameOnCard", fullName);
                data.put("ccNum", ccNum);
                data.put("ExpDate", expiry);
                data.put("cvcField", cvc);
                data.put("type", "Client");
                userRef.set(data, SetOptions.merge());
                Client user = new Client(userRef);
                user.setCC(ccNum, fullName, expiry, cvc);
                // Redirects user to login activity
                startActivity(new Intent(Register3.this, Login.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete incomplete user data when user leaves register activity WITHOUT
                // completing registration activities
                dBase.collection("users").document(mAuth.getCurrentUser().getUid()).delete();
                mAuth.getCurrentUser().delete();
                user = null;

                // Redirects user to login activity WITHOUT completing registration activities
                startActivity(new Intent(Register3.this, Login.class));
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete incomplete user data when user leaves register activity WITHOUT
                // completing registration activities
                dBase.collection("users").document(mAuth.getCurrentUser().getUid()).delete();
                mAuth.getCurrentUser().delete();
                user = null;
                finish();
            }
        });

    }

    private boolean validateNameOnCard(){
        String val = nameOnCardLayout.getEditText().getText().toString().trim();

        if(val.isEmpty()) {
            nameOnCardLayout.setError("Field can not be empty");
            return false;
        }
        else{
            nameOnCardLayout.setError(null);
            nameOnCardLayout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateCardNumber(){
        String val = cardNumberLayout.getEditText().getText().toString().trim();
        String checkOnlyNumbers = "[0-9]";
        if(val.isEmpty()) {
            cardNumberLayout.setError("Field can not be empty");
            return false;
        }
        /* TODO: Field currently does not do anything */
        else if(val.matches(checkOnlyNumbers)) {
            cardNumberLayout.setError("Only numbers are allowed!");
            return false;
        }
        /* TODO: White spaces message only occasionally shows up */
        for (int i = 0; i < val.length(); i++){
            if(val.charAt(i) == ' ' ){
                cvcLayout.setError("No white spaces!");
                return false;
            }
        }
        if(val.length() != 16){
            cardNumberLayout.setError("Field requires 16 numbers");
            return false;
        }
        else{
            cardNumberLayout.setError(null);
            cardNumberLayout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateExpDate(){
        String val = expDateLayout.getEditText().getText().toString().trim();
        String checkOnlyNumbers = "[0-9]";

        if(val.isEmpty()) {
            expDateLayout.setError("Field can not be empty");
            return false;
        }
        else if( val.matches(checkOnlyNumbers)) {
            expDateLayout.setError("Only numbers are allowed!");
            return false;
        }
        for (int i = 0; i < val.length(); i++){
            if(val.charAt(i) == ' ' ){
                cvcLayout.setError("No white spaces!");
                return false;
            }
        }
        if(val.length() != 4 ){
            expDateLayout.setError("Field requires 4 numbers");
            return false;
        }
        else{
            expDateLayout.setError(null);
            expDateLayout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateCvc(){
        String val = cvcLayout.getEditText().getText().toString().trim();
        String checkOnlyNumbers = "[0-9]";

        if(val.isEmpty()) {
            cvcLayout.setError("Field can not be empty");
            return false;
        }
        for(Character i : val.toCharArray()) {
            if(! (i.toString()).matches(checkOnlyNumbers)) {
                cvcLayout.setError("Field must only contain numbers");
            }
        }
        for (int i = 0; i < val.length(); i++){
            if(val.charAt(i) == ' ' ){
                cvcLayout.setError("No white spaces!");
                return false;
            }
        }
        if(val.length() != 3 ){
            cvcLayout.setError("Field requires 3 numbers");
            return false;
        }
        else{
            cvcLayout.setError(null);
            cvcLayout.setErrorEnabled(false);
            return true;
        }
    }
}

