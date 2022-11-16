package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Admin;

import java.time.Duration;

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

        String name = getIntent().getStringExtra("COOK NAME");
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

        suspensionLengthCard = (CardView) findViewById(R.id.suspensionCard);

        suspensionLengthCard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (suspensionLengthCard.getVisibility() != View.GONE) {
                    durationRadioGroup = (RadioGroup) findViewById(R.id.durationRadioGroup);
                    selectDurationButt = (Button) findViewById(R.id.selectDurationButt);

                    selectDurationButt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (durationRadioGroup.getCheckedRadioButtonId() == -1) return;
                            switch (durationRadioGroup.getCheckedRadioButtonId()) {
                                case R.id.oneDay:
                                    try {
                                        Admin.suspendCook(docRef, Duration.ofDays(1));
                                    }catch (Exception e) {
                                        Toast.makeText(ComplaintView.this, "Failed to suspend cook.", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case R.id.sevenDays:
                                    try {
                                        Admin.suspendCook(docRef, Duration.ofDays(7));
                                    }catch (Exception e) {
                                        Toast.makeText(ComplaintView.this, "Failed to suspend cook.", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case R.id.thirtyDays:
                                    try {
                                        Admin.suspendCook(docRef, Duration.ofDays(30));
                                    }catch (Exception e) {
                                        Toast.makeText(ComplaintView.this, "Failed to suspend cook.", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case R.id.indefinite:
                                    try {
                                        Admin.suspendCook(docRef, null);
                                    }catch (Exception e) {
                                        Toast.makeText(ComplaintView.this, "Failed to suspend cook.", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                            }
                            suspensionLengthCard.setVisibility(View.GONE);
                            startActivity(new Intent(ComplaintView.this, AdminHome.class));
                            finish();
                        }
                    });
                }

            }
        });


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
                suspensionLengthCard.setVisibility(View.VISIBLE);
            }
        });
    }
}


