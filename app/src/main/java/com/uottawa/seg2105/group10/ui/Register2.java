package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.group10.R;

public class Register2 extends AppCompatActivity {

    //Initializing buttons
    private Button NextButt;
    private EditText Email;
    private EditText Firstname;
    private EditText Lastname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        //creating option based of off pulled id's
        NextButt = findViewById(R.id.signup_next_button);


        //next button in reg2 send you to reg3 if cook is false (cook created in reg2)
        NextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Email = (EditText)findViewById(R.id.email_text);
                Log.v("Enter Email", Email.getText().toString());

                Firstname = (EditText)findViewById(R.id.signup_fullname);
                Log.v("Enter First name", Email.getText().toString());

                Lastname = (EditText)findViewById(R.id.signup_username);
                Log.v("Enter Last name", Email.getText().toString());

                if (Register1.cook == false) {
                    startActivity(new Intent(Register2.this, Register3.class));
                }
                else{
                    startActivity(new Intent(Register2.this, Register4.class));
                }

        }});



    }
}