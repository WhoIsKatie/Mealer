package com.uottawa.seg2105.group10.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.group10.R;

public class ComplaintView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintview);

        String name = getIntent().getStringExtra("NAME");
        String time = getIntent().getStringExtra("TIME");
        String title = getIntent().getStringExtra("TITLE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String cook = getIntent().getStringExtra("COOK");

        TextView nameTextView = findViewById(R.id.textView9);
        TextView timeTextView = findViewById(R.id.textView10);
        TextView titleTextView = findViewById(R.id.textView12);
        TextView descriptionTextView = findViewById(R.id.textView13);
        TextView cookTextView = findViewById(R.id.textView16);

        nameTextView.setText(name);
        timeTextView.setText(time);
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        cookTextView.setText(cook);
    }


}

