package com.uottawa.seg2105.group10.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.group10.R;

public class Landing extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button registrationButt = findViewById(R.id.registrationButt);
        Button signInButt = findViewById(R.id.signInButt);

        registrationButt.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    startActivity(new Intent(Landing.this, Register1.class));
            }
        });

        signInButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Landing.this, Login.class));
            }
        });


    }
}

