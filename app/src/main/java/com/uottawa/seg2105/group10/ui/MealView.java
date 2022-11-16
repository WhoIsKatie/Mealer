package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;

public class MealView extends AppCompatActivity {

    private static final String TAG = "MEAL_VIEW";
    private DocumentSnapshot document;
    //Initializing buttons
    private Button modifyButt, removeButt;
    private Switch offered;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    DocumentReference firebaseMeal, userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);
        mAuth = FirebaseAuth.getInstance();

        dBase = FirebaseFirestore.getInstance();

        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());


        String name = getIntent().getStringExtra("NAME");
        float price = getIntent().getFloatExtra("PRICE", 0);
        String image = getIntent().getStringExtra("IMAGE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String docID = getIntent().getStringExtra("DOCUMENT");

        //DocumentReference docRef = dBase.collection("meals").document(docID);

        TextView nameTextView = findViewById(R.id.mealName);
        TextView priceTextView = findViewById(R.id.mealPrice);
        TextView descriptionTextView = findViewById(R.id.mealDesc);
        ImageView mealImageView = findViewById(R.id.mealImage);

        nameTextView.setText(name);
        priceTextView.setText(Float.toString(price));
        descriptionTextView.setText(description);
        //mealImageView.setImageResource(Integer.parseInt(image)); todo: image doesn't work since the int is supposed to refer to a drawable
        //mealImageView.setImageURI();
        modifyButt = findViewById(R.id.modifyButt);
        removeButt = findViewById(R.id.removeButt);

        modifyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MealView.this, AddMeal.class));
                finish();
            }
        });

        removeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                //System.out.println(userRef.collection("meals").document(mAuth.getCurrentUser().getUid()));
                userRef.collection("meals").document(docID).delete();
                finish();
            }
        });
    }
}