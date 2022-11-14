package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    private FirebaseFirestore dBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);

        dBase = FirebaseFirestore.getInstance();

        String name = getIntent().getStringExtra("NAME");
        float price = getIntent().getFloatExtra("PRICE", 0);
        int image = getIntent().getIntExtra("IMAGE", 0);
        String description = getIntent().getStringExtra("DESCRIPTION");
        String docID = getIntent().getStringExtra("DOCUMENT");

        DocumentReference docRef = dBase.collection("meals").document(docID);

        TextView nameTextView = findViewById(R.id.mealName);
        TextView priceTextView = findViewById(R.id.mealPrice);
        TextView descriptionTextView = findViewById(R.id.mealDesc);
        ImageView mealImageView = findViewById(R.id.mealImage);

        nameTextView.setText(name);
        priceTextView.setText(Float.toString(price));
        descriptionTextView.setText(description);
        mealImageView.setImageResource(image);
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
                /*TODO: Implement code to remove a meal from the menu*/            }
        });
    }
}