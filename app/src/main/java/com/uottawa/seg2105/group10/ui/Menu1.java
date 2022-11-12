package com.uottawa.seg2105.group10.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.group10.R;

public class Menu1 extends AppCompatActivity {

    private ImageView nextButt;


    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);
/*
        nextButt = findViewById(R.id.preMenuNextButt);

        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu1.this, Menu.class));
            }
        });*/
    }


}