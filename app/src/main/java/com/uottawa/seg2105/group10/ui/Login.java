package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uottawa.seg2105.group10.Mealer;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.ui.adminView.AdminHome;
import com.uottawa.seg2105.group10.ui.clientView.ClientHome;
import com.uottawa.seg2105.group10.ui.cookView.CookHome;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

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
        LoginViewModel model = new ViewModelProvider(this).get(LoginViewModel.class);

        //initializing
        mAuth = FirebaseAuth.getInstance();

        // 'Login' button launches user's homepage respective to type.
        Button login = findViewById(R.id.letTheUserLogIn);
        ImageButton back = findViewById(R.id.back);
        EditText login_username = findViewById(R.id.loginUserEditText);
        EditText login_password = findViewById(R.id.loginPassEditText);

        usernameLayout = findViewById(R.id.loginUserLayout);
        passwordLayout = findViewById(R.id.loginPassLayout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // retrieving user input from fields for username and password
                email = login_username.getText().toString().trim();
                password = login_password.getText().toString().trim();

                if(!validateEmail() | !validatePassword()) {
                    return;
                }
                model.login(email, password).observe(Login.this, user -> {
                    if (user != null)
                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(user);
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
            Toast.makeText(this, "Welcome back to Mealer!", Toast.LENGTH_LONG).show();
            Mealer app = (Mealer) getApplicationContext();
            app.initializeUser();
            switch (app.getType()) {
                case "Admin":
                    startActivity(new Intent(this, AdminHome.class));
                    break;
                case "Cook":
                    startActivity(new Intent(this, CookHome.class));
                    break;
                default:
                    startActivity(new Intent(this, ClientHome.class));
            }
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