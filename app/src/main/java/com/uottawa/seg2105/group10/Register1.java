package com.uottawa.seg2105.group10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Register1 extends AppCompatActivity {

    private Button cookOption, clientOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        cookOption = findViewById(R.id.cookOption);
        cookOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register1.this, Register2.class));
            }
        });

        clientOption = findViewById(R.id.clientOption);
        clientOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register1.this, Register2.class));
            }
        });
    }
}