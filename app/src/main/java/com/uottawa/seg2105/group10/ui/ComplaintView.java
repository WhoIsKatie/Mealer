package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Admin;

public class ComplaintView extends AppCompatActivity {

    private static final String TAG = "COMPLAINT_VIEW";
    private DocumentSnapshot document;
    //Intializing butons
    private Button dismissButt, suspendButt, selectDurationButt;
    private CardView suspensionLengthCard;
    private RadioGroup durationRadioGroup;
    private RadioButton oneDay, sevenDays, thirtyDays, indefinite;

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
                Admin.dismissComplaint(docRef);
                startActivity(new Intent(ComplaintView.this, AdminHome.class));
                finish();
            }
        });

        suspendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                durationRadioGroup = findViewById(R.id.durationRadioGroup);
                selectDurationButt = findViewById(R.id.selectDurationButt);
                suspensionLengthCard = (CardView) findViewById(R.id.suspensionCard);
                /*Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();*/
                selectDurationButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (durationRadioGroup.getCheckedRadioButtonId() == -1) return;
                        switch (durationRadioGroup.getCheckedRadioButtonId()) {
                            case R.id.oneDay:
                                //Admin.suspendCook(docRef, Duration.ofDays(1));
                                break;
                            case R.id.sevenDays:
                                //Admin.suspendCook(docRef, Duration.ofDays(7));
                                break;
                            case R.id.thirtyDays:
                                //Admin.suspendCook(docRef, Duration.ofDays(30));
                                break;
                            case R.id.indefinite:
                                //Admin.suspendCook(docRef);
                                break;
                        }
                    }
                });

                startActivity(new Intent(ComplaintView.this, AdminHome.class));
                finish();
            }
        });
    }
}


