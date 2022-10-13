package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.User;

public class Register4 extends AppCompatActivity {

    private Button nextButt;
    private Button login;

    private TextInputEditText profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);
        User user = Register2.user;
        nextButt = findViewById(R.id.signup_submit_button);
        login = findViewById(R.id.signup_login_button2);
        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = (TextInputEditText) findViewById(R.id.profileDescUpper);
                String profDesc = profile.getText().toString();


                //((Cook) user).setCC(profDesc, JAKE PUT STUFF HERE);

            }



            //TODO: use Client.java setCC method to complete profile

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register4.this, Login.class));
            }

        });

    }
}