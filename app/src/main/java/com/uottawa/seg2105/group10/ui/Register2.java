package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.uottawa.seg2105.group10.R;

public class Register2 extends AppCompatActivity {

    //Initializing buttons
    private Button nextButt;
    private TextInputEditText emailField;
    private TextInputEditText firstNameField;
    private TextInputEditText lastNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        //creating option based of off pulled id's
        nextButt = findViewById(R.id.signup_next_button);


        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                emailField = (TextInputEditText)findViewById(R.id.email_text);
                String email = emailField.getText().toString();

                firstNameField = (TextInputEditText)findViewById(R.id.signup_fullname);
                String firstName = emailField.getText().toString();

                lastNameField = (TextInputEditText)findViewById(R.id.signup_username);
                String lastName = emailField.getText().toString();

                //Directs user to step 2 of registration process
                //If user is a cook, directs to Register3 activity. Otherwise, user is directed to Register4 activity
                if (Register1.cook) {
                    startActivity(new Intent(Register2.this, Register3.class));
                }
                else{
                    startActivity(new Intent(Register2.this, Register4.class));
                }

        }});



    }
}