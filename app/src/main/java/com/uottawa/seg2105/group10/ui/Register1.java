package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.group10.R;

public class Register1 extends AppCompatActivity {


    //Initializing buttons
    private Button cookOption, clientOption;


    //if cook == true than you are a cook
    //if cook == false than you are a client
    public static boolean cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        //creating option based of off pulled id's
        cookOption = findViewById(R.id.cookOption);
        clientOption = findViewById(R.id.clientOption);



        //cook button in register1 that directs you to register2
        //when cook button is clicked, cook is set  to ture
        cookOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cook = true;
                startActivity(new Intent(Register1.this, Register2.class));
            }
        });


        //client button in register1 that directs you to register2
        //when client button is clicked, cook is set to false
        clientOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cook = false;
                startActivity(new Intent(Register1.this, Register2.class));
            }
        });
    }
}