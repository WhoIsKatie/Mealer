package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        User user = Register2.user;

        nextButt = findViewById(R.id.clientSubmitButt);
        login = findViewById(R.id.reg3LoginButt);
        back = findViewById(R.id.reg3BackButt);

        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

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

                ((Client) Register2.user).setCC(ccNum, fullName, expiry, cvc);

                // Add user document with Uid set as document ID to collection of "users" in Firestore
                DocumentReference userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                userRef.set(Register2.user);
                // Update CC fields
                Map<String, Object> data = new HashMap<>();
                data.put("nameOnCard", fullName);
                data.put("ccNum", ccNum);
                data.put("ExpDate", expiry);
                data.put("cvcField", cvc);

                userRef.set(data, SetOptions.merge());
                // Redirects user to login activity
                startActivity(new Intent(Register3.this, Login.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete incomplete user data when user leaves register activity WITHOUT
                // completing registration activities
                dBase.collection("users").document(mAuth.getCurrentUser().getUid()).delete();
                mAuth.getCurrentUser().delete();
                Register2.user = null;

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
                Register2.user = null;
                finish();
            }
        });

    }
}

