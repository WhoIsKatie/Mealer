package com.uottawa.seg2105.group10.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.uottawa.seg2105.group10.R;

public class Login extends AppCompatActivity {

    private Button letTheUserLogIn; //button that says 'Login' which should bring to welcome page
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        letTheUserLogIn = findViewById(R.id.letTheUserLogIn);
        letTheUserLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // brings user to welcome class, needs to be fixed to get info from firebase
                startActivity(new Intent(Login.this, Welcome.class));
                // finish will kill the login activity so user cannot return back here
                //finish();
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if back is clicked this activity just ends
                finish();
            }
        });
    }


}