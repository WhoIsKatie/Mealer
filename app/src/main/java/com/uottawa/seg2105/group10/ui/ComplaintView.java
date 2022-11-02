package com.uottawa.seg2105.group10.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;

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

        String name = getIntent().getStringExtra("NAME");
        String time = getIntent().getStringExtra("TIME");
        String title = getIntent().getStringExtra("TITLE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String cook = getIntent().getStringExtra("COOK");
        String docment = getIntent().getStringExtra("DOCUMENT");

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

        dismissButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = dBase.collection("complaints").document(docment);
                docRef.update("status", true);

              //  ApiFuture<WriteResult> complainStatus = docRef.update("status", true);
                //docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  //  @Override
                    //public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      //  DocumentSnapshot document = task.getResult();
                        //if(document.exists()){
                          //  Log.d("status", "true");
 //                       }
   //                 }
     //           });


            }
        });


    }
}


