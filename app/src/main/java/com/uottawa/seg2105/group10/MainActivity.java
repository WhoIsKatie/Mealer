package com.uottawa.seg2105.group10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button registrationButt, signInButt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registrationButt = findViewById(R.id.registrationButt);
        signInButt = findViewById(R.id.signInButt);

        registrationButt.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, Register.class));
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

