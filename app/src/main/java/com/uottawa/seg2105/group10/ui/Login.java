package com.uottawa.seg2105.group10.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uottawa.seg2105.group10.R;

public class Login extends AppCompatActivity {

    private Button registrationButt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registrationButt2 = findViewById(R.id.registrationButt2);
        registrationButt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register1.class));
            }
        });
    }


}