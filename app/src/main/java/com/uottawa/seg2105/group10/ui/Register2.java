package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Client;
import com.uottawa.seg2105.group10.backend.Cook;
import com.uottawa.seg2105.group10.backend.User;

public class Register2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    //Initializing buttons
    private Button nextButt;
    private TextInputEditText emailField, firstNameField, lastNameField, passField, addressField;

    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        // Initialize FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        // creating option based of off pulled id's
        nextButt = findViewById(R.id.why_me);



        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                // collecting user information from text fields
                firstNameField = (TextInputEditText)findViewById(R.id.firstname_text);
                String firstName = firstNameField.getText().toString();

                lastNameField = (TextInputEditText)findViewById(R.id.lastName_text);
                String lastName = lastNameField.getText().toString();

                emailField = (TextInputEditText)findViewById(R.id.email_text);
                String email = emailField.getText().toString();

                passField = (TextInputEditText)findViewById(R.id.pass_text);
                String password = passField.getText().toString();

                addressField = (TextInputEditText)findViewById(R.id.address_text);
                String address = addressField.getText().toString();

                // Create Firebase user account with email and password
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register2.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // registration success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
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
                            public void updateUI(FirebaseUser account){

                                if(account != null){
                                    // Initializes a Mealer user object WITH Firebase user object
                                    // Directs user to step 2 of registration process
                                    // If user is NOT a cook, directs to Register3 activity. Otherwise, user is directed to Register4 activity
                                    if (!Register1.isCook()) {
                                        user = new Client(account, firstName, lastName, email, password, address);
                                        startActivity(new Intent(Register2.this, Register3.class));
                                    }
                                    else{
                                        user = new Cook(account, firstName, lastName, email, password, address);
                                        startActivity(new Intent(Register2.this, Register4.class));
                                    }
                                }

                            }
                        });

        }});
    }

    // Getter method for instance of Mealer user
}