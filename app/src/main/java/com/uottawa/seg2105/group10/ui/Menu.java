package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.group10.R;


public class Menu extends AppCompatActivity {
    private Button addMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        addMeal = findViewById(R.id.addMeal);
        addMeal.setOnClickListener(view -> {
            startActivity(new Intent(Menu.this, AddMeal.class));
        });
    }
}