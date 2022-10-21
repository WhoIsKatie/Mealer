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


    //if cook == true, user is cook
    //if cook == false, user is  client
    private static boolean cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        //creating option based of off pulled id's
        cookOption = findViewById(R.id.cookOptionButt);
        clientOption = findViewById(R.id.clientOptionButt);



        //cook button in register1 that directs you to register2
        //when cook button is clicked, cook is set  to true
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

    // verifies if user selection is cook in remaining registration activities
    public static Boolean isCook() {
        return cook;
    }
}