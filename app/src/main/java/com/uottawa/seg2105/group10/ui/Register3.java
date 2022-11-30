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
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Client;
import com.uottawa.seg2105.group10.backend.User;

import java.util.Map;

public class Register3 extends AppCompatActivity {

    private static final String TAG = "Register3";

    //Initializing buttons
    private Button nextButt;
    private Button login;
    private ImageButton back;
    private TextInputEditText nameOnCard, CardNumber, expDate, cvcField;

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

                expDate = (TextInputEditText) findViewById(R.id.expiryEditText);
                String expiry = expDate.getText().toString();

                cvcField = (TextInputEditText) findViewById(R.id.cvcEditText);
                String cvc = cvcField.getText().toString();

                // Verifies if all fields have met specifications
                if(!validateNameOnCard() | !validateCardNumber() | !validateExpDate() | !validateCvc()) {
                    return;
                }

                // Add user document with Uid set as document ID to collection of "users" in Firestore
                DocumentReference userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                // Update CC fields
                Map<String, String> data = Register2.data;
                data.put("nameOnCard", fullName);
                data.put("ccNum", ccNum);
                data.put("expDate", expiry);
                data.put("cvcField", cvc);
                data.put("type", "Client");

                user = new Client(data);
                userRef.set(user);
                //userRef.set(data);
                //user = new Client(userRef);
                // adding a sub-collection to user document to keep Mealer User object and DateTime suspensionEnd
                //userRef.collection("userObject").document("Client").update("User", user);

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

    // CC Helper Methods -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    // Returns true if card-holder name length is 1-30 characters; returns false otherwise.
    private boolean validateNameOnCard(){
        String val = nameOnCardLayout.getEditText().getText().toString().trim();

        if(val.isEmpty()) {
            nameOnCardLayout.setError("Field cannot be empty");
            return false;
        }
        if(val.length() > 30 ){
            nameOnCardLayout.setError("Field must not go over 30 characters");
            return false;
        }
        else{
            nameOnCardLayout.setError(null);
            nameOnCardLayout.setErrorEnabled(false);
            return true;
        }
    }

    // Returns true if card number only contains 16 integers; returns false otherwise.
    private boolean validateCardNumber(){
        String val = cardNumberLayout.getEditText().getText().toString().trim();
        String numFormat = "[0-9]{16}";
        if(val.isEmpty()) {
            cardNumberLayout.setError("Field cannot be empty");
            return false;
        }
        if(!val.matches(numFormat)) {
            cardNumberLayout.setError("Invalid card number!");
            return false;
        }
        cardNumberLayout.setError(null);
        cardNumberLayout.setErrorEnabled(false);
        return true;

    }

    // Returns true if expiry-date is of MM/YY format; returns false otherwise.
    private boolean validateExpDate(){
        String val = expDateLayout.getEditText().getText().toString().trim();
        String dateFormat = "(?:0[1-9]|1[0-2])[0-9]{2}";

        if(val.isEmpty()) {
            expDateLayout.setError("Field cannot be empty");
            return false;
        }
        if(!val.matches(dateFormat)) {
            expDateLayout.setError("Invalid date! Must be of MMYY format.");
            return false;
        }
        expDateLayout.setError(null);
        expDateLayout.setErrorEnabled(false);
        return true;

    }

    // Returns true if cvc is 3 integers; returns false otherwise.
    private boolean validateCvc(){
        String val = cvcLayout.getEditText().getText().toString().trim();
        String cvcFormat = "[0-9]{3}";

        if(val.isEmpty()) {
            cvcLayout.setError("Field cannot be empty");
            return false;
        }
        if(!val.matches(cvcFormat)) {
            cvcLayout.setError("Invalid security code!");
            return false;
        }
        cvcLayout.setError(null);
        cvcLayout.setErrorEnabled(false);
        return true;
    }
}

