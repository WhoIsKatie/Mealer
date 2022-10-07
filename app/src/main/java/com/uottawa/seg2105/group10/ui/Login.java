package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.uottawa.seg2105.group10.R;

public class Login extends AppCompatActivity {

    private Button letTheUserLogIn; //button that says 'Login' which should bring to welcome page
    private ImageButton back;
    private FirebaseAuth mAuth;
    private TextInputEditText login_username, login_password2;
    private static final String TAG = "EmailPassword";

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing
        mAuth = FirebaseAuth.getInstance();
        letTheUserLogIn = findViewById(R.id.letTheUserLogIn);
        back = findViewById(R.id.back);
        EditText login_username = findViewById(R.id.login_username);
        EditText login_password2 = findViewById(R.id.login_password2);

        letTheUserLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting fields for username and password
                email = login_username.getText().toString().trim();
                password = login_password2.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if back is clicked this activity just ends
                finish();
            }
        });


    }

    public void onStart() {
        super.onStart();
        //checking if user is signed in already
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            currentUser.reload();
        }
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, "Welcome back to Mealer", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Welcome.class));
        }
    }


}