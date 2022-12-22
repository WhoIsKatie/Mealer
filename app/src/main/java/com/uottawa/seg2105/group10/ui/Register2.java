package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.User;

import java.util.HashMap;
import java.util.Map;

public class Register2 extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "Register2";

    //Initializing buttons
    private Button nextButt;
    private ImageButton back;
    private TextInputEditText emailField, firstNameField, lastNameField, passField, addressField;
    private Button login;
    protected static Map<String, String> data;
    protected static User newUser;

    TextInputLayout firstName, lastName, username, password, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        // Initialize Firebase Authority and Firebase Firestore objects
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();


        // creating option based off of pulled id's
        nextButt = findViewById(R.id.whyJacobButt);
        login = findViewById(R.id.reg2LoginButt);
        back = findViewById(R.id.reg2BackButt);

        // initialize EditText fields
        firstName = findViewById(R.id.firstNameLayout);
        lastName = findViewById(R.id.lastNameLayout);
        username = findViewById(R.id.emailLayout);
        password = findViewById(R.id.passLayout);
        address = findViewById(R.id.addressLayout);

        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                // collecting user information from text fields
                firstNameField = (TextInputEditText)findViewById(R.id.firstNameEditText);
                String firstName = firstNameField.getText().toString();

                lastNameField = (TextInputEditText)findViewById(R.id.lastNameEditText);
                String lastName = lastNameField.getText().toString();

                emailField = (TextInputEditText)findViewById(R.id.emailEditText);
                String email = emailField.getText().toString();

                passField = (TextInputEditText)findViewById(R.id.passEditText);
                String password = passField.getText().toString();

                addressField = (TextInputEditText)findViewById(R.id.addressEditText);
                String address = addressField.getText().toString();

                if(!validateFirstName() | !validateLastName() | !validateEmail() | !validatePassword() | !validateAddress()) {
                    return;
                }

                // Create Firebase user account with email and password
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register2.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // registration success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileCompletion = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(data.get("firstName") + " " + data.get("lastName")).build();
                                    if (user != null)
                                        user.updateProfile(profileCompletion);
                                    updateUI(user);
                                } else {
                                    // If registration fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register2.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }

                            //Change UI according to user data.
                            // if new register 2 is made then static var data will be reset
                            public void updateUI(FirebaseUser account){
                                if(account != null){
                                    data = new HashMap<>();
                                    data.put("firstName", firstName);
                                    data.put("lastName", lastName);
                                    data.put("email", email);
                                    data.put("password", password);
                                    data.put("address", address);
                                    data.put("uid", account.getUid());

                                    // Directs user to step 2 of registration process:
                                    // If user is NOT a cook, directs to Register3 activity.
                                    // Otherwise, user is directed to Register4 activity.
                                    startActivity(new Intent(Register2.this,
                                            (!Register1.isCook() ? Register3.class : Register4.class)));
                                }

                            }
                        });

        }});

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register2.this, Login.class));
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Profile Field Helper Methods -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    // Returns true if name length is 1-30 characters; returns false otherwise.
    private boolean validateFirstName(){
        String val = firstName.getEditText().getText().toString().trim();

        if(val.isEmpty()) {
            firstName.setError("Field can not be empty");
            return false;
        }
        if(val.length() > 30 ){
            firstName.setError("Field must not go over 30 characters");
            return false;
        }
        else{
            firstName.setError(null);
            firstName.setErrorEnabled(false);
            return true;
        }
    }

    // Returns true if name length is 1-30 characters; returns false otherwise.
    private boolean validateLastName(){
        String val = lastName.getEditText().getText().toString().trim();

        if(val.isEmpty()) {
            lastName.setError("Field can not be empty");
            return false;
        }
        if(val.length() > 30 ){
            lastName.setError("Field must not go over 30 characters");
            return false;
        }
        else{
            lastName.setError(null);
            lastName.setErrorEnabled(false);
            return true;
        }
    }

    // Returns true if address is 1-35 characters; returns false otherwise.
    private boolean validateAddress(){
        String val = address.getEditText().getText().toString().trim();

        if(val.isEmpty()) {
            address.setError("Field can not be empty");
            return false;
        }
        if(val.length() > 35 ){
            address.setError("Field must not go over 35 characters");
            return false;
        }
        else{
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(){
        String val = username.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if(val.isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        }
        else if(! val.matches(checkEmail)){
            username.setError("Invalid email!");
            return false;
        }
        else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    // Returns true if password length is 8-30 characters, contains >=1 numbers,
    // and >=1 special characters; returns false otherwise.
    private boolean validatePassword(){
        String val = password.getEditText().getText().toString().trim();
        String checkNumeric = ".*[0-9].*";
        String checkSymbol = ".*[!@#$%^&=?+_].*";

        if(val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        }

        if(val.length() < 8 || val.length() > 20 ){
            password.setError("Field must be between 8 and 20 characters");
            return false;
        }
        if(!val.matches(checkNumeric)){
            password.setError("Field must contain at least 1 number");
            return false;
        }
        if(!val.matches(checkSymbol)){
            password.setError("Field must contain at least 1 special character");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}