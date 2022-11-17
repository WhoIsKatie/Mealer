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
import com.uottawa.seg2105.group10.backend.Meal;

import java.util.HashMap;

public class MealView extends AppCompatActivity {

    private static final String TAG = "MEAL_VIEW";
    private DocumentSnapshot document;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    DocumentReference firebaseMeal, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        String name = getIntent().getStringExtra("MEAL NAME");
        float price = getIntent().getFloatExtra("PRICE", 0);
        String image = getIntent().getStringExtra("IMAGE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        Intent intent = getIntent();
        HashMap<String, String> ingredient = (HashMap<String, String>)intent.getSerializableExtra("INGREDIENTS");
        //Log.v("HashMapTest", ingredient.get(name));


        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
        firebaseMeal = userRef.collection("meals").document(name);

        TextView nameTextView = findViewById(R.id.mealName);
        TextView priceTextView = findViewById(R.id.mealPrice);
        TextView descriptionTextView = findViewById(R.id.mealDesc);
        ImageView mealImageView = findViewById(R.id.mealImage);
        TextView ingredentTextView = findViewById(R.id.ingredentsview);

        nameTextView.setText(name);
        priceTextView.setText(Float.toString(price));
        descriptionTextView.setText(description);
        ingredentTextView.setText(ingredient.get("ingredients"));
        //mealImageView.setImageResource(Integer.parseInt(image)); todo: image doesn't work since the int is supposed to refer to a drawable
        //mealImageView.setImageURI();
        //Initializing buttons
        Button modifyButt = findViewById(R.id.modifyButt);
        Button removeButt = findViewById(R.id.removeButt);
        Switch offerToggle = findViewById(R.id.offerToggle);

        modifyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealView.this, AddMeal.class);
                intent.putExtra("Name", name);
                intent.putExtra("Price", Double.toString(price));
                intent.putExtra("Description", description);
                startActivity(intent);
                finish();
            }
        });

        removeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                //System.out.println(userRef.collection("meals").document(mAuth.getCurrentUser().getUid()));
                userRef.collection("meals").document(name).delete();
                finish();
            }
        });

        offerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseMeal.get().addOnSuccessListener(snapshot -> {
                    Meal thisMeal = snapshot.toObject(Meal.class);
                    if (thisMeal.isOffered())
                        thisMeal.stopOffering();
                    else
                        thisMeal.offerMeal();
                    firebaseMeal.set(thisMeal);
                });
            }
        });
    }
}