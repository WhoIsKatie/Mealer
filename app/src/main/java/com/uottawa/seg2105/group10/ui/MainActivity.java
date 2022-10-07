package com.uottawa.seg2105.group10.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.group10.R;

public class MainActivity extends AppCompatActivity {

    private Button registrationButt, signInButt;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: check to see if the user is currently signed in
        /*super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }*/
        setContentView(R.layout.activity_main);

        registrationButt = findViewById(R.id.registrationButt);
        signInButt = findViewById(R.id.signInButt);


        registrationButt.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, Register1.class));
            }
        });

        signInButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });


    }
}

