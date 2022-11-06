package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.temp.ComplaintModel;

public class ComplaintView extends AppCompatActivity {


    private DocumentSnapshot document;
    //Intializing butons
    private Button dismissButt;

    private FirebaseFirestore dBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintview);
        dBase = dBase.getInstance();

        /*String name = getIntent().getStringExtra("NAME");
        String time = getIntent().getStringExtra("TIME");
        String title = getIntent().getStringExtra("TITLE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String cook = getIntent().getStringExtra("COOK");*/
        ComplaintModel complaint = getIntent().getParcelableExtra("DOCUMENT");

        TextView nameTextView = findViewById(R.id.textView9);
        TextView timeTextView = findViewById(R.id.textView10);
        TextView titleTextView = findViewById(R.id.textView12);
        TextView descriptionTextView = findViewById(R.id.textView13);
        TextView cookTextView = findViewById(R.id.textView16);

        nameTextView.setText(complaint.getNameOfCook());
        timeTextView.setText(complaint.getTimeOfComplaint());
        titleTextView.setText(complaint.getTitleOfComplaint());
        descriptionTextView.setText(complaint.getDescriptionOfComplaint());
        cookTextView.setText(complaint.getCookUid());
        dismissButt = findViewById(R.id.dismissButt);

        dismissButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = complaint.getDocument();
                Task task = docRef.update("status", false);
                task.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        startActivity(new Intent(ComplaintView.this, AdminHome.class));
                    }
                });
            }
        });


    }
}


