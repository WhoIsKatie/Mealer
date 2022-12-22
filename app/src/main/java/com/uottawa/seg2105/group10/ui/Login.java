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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;

public class Login extends AppCompatActivity {

    private Button letTheUserLogIn; //button that says 'Login' which should bring to welcome page
    private ImageButton back;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "EmailPassword";

    private String email, password;

    TextInputLayout usernameLayout, passwordLayout;

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing
        mAuth = FirebaseAuth.getInstance();
        letTheUserLogIn = findViewById(R.id.letTheUserLogIn);
        back = findViewById(R.id.back);
        EditText login_username = findViewById(R.id.loginUserEditText);
        EditText login_password2 = findViewById(R.id.loginPassEditText);

        usernameLayout = findViewById(R.id.loginUserLayout);
        passwordLayout = findViewById(R.id.loginPassLayout);

        letTheUserLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting fields for username and password
                // idea from https://stackoverflow.com/questions/37352871/firebase-9-0-0-mauth-signinwithemailandpassword-how-to-pass-it-to-a-button
                // bug fix from https://stackoverflow.com/questions/39486937/getting-cannot-resolve-method-addoncompletionlistener-while-trying
                email = login_username.getText().toString().trim();
                password = login_password2.getText().toString().trim();

                if(!validateEmail() | !validatePassword()) {
                    return;
                }
                FirebaseUser user = mAuth.getCurrentUser();
                // this is inside onclick so it doesn't run immediately when the activity begins
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
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
                // if back button is clicked, login activity ends
                startActivity(new Intent(Login.this, Landing.class));
                finish();
            }
        });
    }

    public void onStart() {
        super.onStart();
        //checks if user is signed in already
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            currentUser.reload();
        }
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            // if valid user is passed, they have signed in
            // direct user to welcome page and notify with toast
            Toast.makeText(this, "Welcome back to Mealer", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Welcome.class));
            finish();
        }
    }

    private boolean validateEmail(){
        String val = usernameLayout.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if(val.isEmpty()) {
            usernameLayout.setError("Field can not be empty");
            return false;
        }
        else if(! val.matches(checkEmail)){
            usernameLayout.setError("Invalid email!");
            return false;
        }
        else{
            usernameLayout.setError(null);
            usernameLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String val = passwordLayout.getEditText().getText().toString().trim();
        String checkNumeric = ".*[0-9].*";
        String checkSymbol = ".*[!@#$%^&=?+].*";

        if(val.isEmpty()) {
            passwordLayout.setError("Field can not be empty");
            return false;
        }

        if(val.length() < 8 || val.length() > 20 ){
            passwordLayout.setError("Field must be between 8 and 20 characters");
            return false;
        }
        if(!val.matches(checkNumeric)){
            passwordLayout.setError("Field must contain at least 1 number");
            return false;
        }
        if(!val.matches(checkSymbol)){
            passwordLayout.setError("Field must contain at least 1 special character");
            return false;
        }
        else{
            passwordLayout.setError(null);
            passwordLayout.setErrorEnabled(false);
            return true;
        }
    }
}