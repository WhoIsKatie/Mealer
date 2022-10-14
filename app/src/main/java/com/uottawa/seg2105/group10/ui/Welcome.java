package com.uottawa.seg2105.group10.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Client;
import com.uottawa.seg2105.group10.backend.User;

public class Welcome extends AppCompatActivity {

    private TextView typeText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        typeText = findViewById(R.id.userTypeText);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        dBase = FirebaseFirestore.getInstance();
        DocumentReference userDoc = dBase.collection("users").document(user.getUid());
        DocumentSnapshot document = userDoc.get().getResult();


        if (user.getEmail() == "jacobmaurice2003@gmail.com"){
            typeText.setText("@string/admin");
        }
        else if(document.get("type") == "Cook") {
            typeText.setText("@string/cook");
        }
    }
}