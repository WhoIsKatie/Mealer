package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;

public class ComplaintView extends AppCompatActivity {


    private DocumentSnapshot document;
    //Intializing butons
    private Button dismissButt, suspendButt;
    private RadioGroup suspendTimes;
    private RadioButton fiveday, tenday, indef;

    private FirebaseFirestore dBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintview);
        dBase = dBase.getInstance();

        String name = getIntent().getStringExtra("NAME");
        String time = getIntent().getStringExtra("TIME");
        String title = getIntent().getStringExtra("TITLE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String cook = getIntent().getStringExtra("COOK");
        String docID = getIntent().getStringExtra("DOCUMENT");

        DocumentReference docRef = dBase.collection("complaints").document(docID);

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
        dismissButt = findViewById(R.id.dismissButt);
        suspendButt = findViewById(R.id.suspendButt);

        dismissButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRef.update("status", false);
                startActivity(new Intent(ComplaintView.this, ComplaintView.class));
                finish();
            }
        });

        suspendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRef.update("suspended", true);
                docRef.update("releaseDays", 10);
                //TODO: change how release days is done!!
                docRef.update("status", false);

                //TODO: set how long suspension is for.
                //suspendTimes.setVisibility(View.VISIBLE);
                /*fiveday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DateTimeFormatter dtf = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            LocalDateTime now = LocalDateTime.now();
                            //dtf.format(now);
                            docRef.update("releaseDate", dtf.format(now));
                        }
                        startActivity(new Intent(ComplaintView.this, ComplaintView.class));
                        finish();
                    }
                }); */
                startActivity(new Intent(ComplaintView.this, ComplaintView.class));
                finish();
            }
        });

    }
}


