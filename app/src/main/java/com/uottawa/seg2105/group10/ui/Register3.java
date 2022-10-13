package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Client;
import com.uottawa.seg2105.group10.backend.User;

public class Register3 extends AppCompatActivity {



    //Initializing buttons
    private Button nextButt;
    private Button login;
    private TextInputEditText nameOnCard, CardNumber, ExpDate, SecCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        nextButt = findViewById(R.id.signup_submit_button);
        login = findViewById(R.id.signup_login_button2);


        User user = Register2.user;
        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameOnCard = (TextInputEditText) findViewById(R.id.signup_username);
                String firstName = nameOnCard.getText().toString();

                CardNumber = (TextInputEditText) findViewById(R.id.signup_password);
                String ccNum = CardNumber.getText().toString();

                ExpDate = (TextInputEditText) findViewById(R.id.editTextNumber);
                String email = ExpDate.getText().toString();

                SecCode = (TextInputEditText) findViewById(R.id.editTextNumber2);
                String password = SecCode.getText().toString();

                ((Client) user).setCC(ccNum, firstName, email, password);
                startActivity(new Intent(Register3.this, Login.class));

            }


        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register3.this, Login.class));
            }

        });

    }
}

