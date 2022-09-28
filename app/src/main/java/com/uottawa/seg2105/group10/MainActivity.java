package com.uottawa.seg2105.group10;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (TextView) findViewById(R.id.button);
        button.setOnClickListener(this);
    }
}

    @Override
    public void onCLick(View v){
    switch (v.getId()){
        case R.id.button:
            startActivity(new Intent(packageContext:));
    }
    }