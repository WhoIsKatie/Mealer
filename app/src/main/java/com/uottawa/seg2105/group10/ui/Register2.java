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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Client;
import com.uottawa.seg2105.group10.backend.Cook;
import com.uottawa.seg2105.group10.backend.User;

public class Register2 extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "Register2";

    //Initializing buttons
    private Button nextButt;
    private ImageButton back;
    private TextInputEditText emailField, firstNameField, lastNameField, passField, addressField;
    private Button login;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        // Initialize FirebaseAuth and FirebaseFirestore objects
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        // creating option based of off pulled id's
        nextButt = findViewById(R.id.whyJacobButt);
        login = findViewById(R.id.reg2LoginButt);
        back = findViewById(R.id.reg2BackButt);



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
                                    // Initializes a Mealer user object
                                    if (!Register1.isCook()) {
                                        user = new Client(firstName, lastName, email, password, address);
                                    } else {
                                        user = new Cook(firstName, lastName, email, password, address);
                                    }

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
}