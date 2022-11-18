package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;

import java.util.ArrayList;

public class MealView extends AppCompatActivity {

    private static final String TAG = "MEAL_VIEW";
    private DocumentSnapshot document;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    DocumentReference firebaseMeal, userRef;
    String  visibleIngredents, temp1,  visibleCuisine, temp2,  visibleAllergens, temp3;
    Switch offerToggle;

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
        ArrayList<String> ingredients = getIntent().getStringArrayListExtra("INGREDIENTS");
        ArrayList<String> allergens = getIntent().getStringArrayListExtra("ALLERGENS");
        ArrayList<String> cuisine = getIntent().getStringArrayListExtra("CUISINE");
        String mealType = getIntent().getStringExtra("MEAL TYPE");

        //Initializing buttons
        Button modifyButt = findViewById(R.id.modifyButt);
        Button removeButt = findViewById(R.id.removeButt);
        offerToggle = findViewById(R.id.offerToggle);

        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
        firebaseMeal = userRef.collection("meals").document(name);

        TextView nameTextView = findViewById(R.id.mealName);
        TextView ingreidnentTextView = findViewById(R.id.ingredentVeiw);
        TextView cuisineTextView = findViewById(R.id.cuisineView);
        TextView allergensTextView = findViewById(R.id.allergensView);
        TextView meatTypeTextView = findViewById(R.id.mealTypeview);
        TextView priceTextView = findViewById(R.id.mealPrice);
        TextView descriptionTextView = findViewById(R.id.mealDesc);
        ImageView mealImageView = findViewById(R.id.mealImage);

        meatTypeTextView.setText(mealType);
        nameTextView.setText(name);
        priceTextView.setText(Float.toString(price));
        descriptionTextView.setText(description);
        ingreidnentTextView.setText(createTextViewForIngredient(ingredients));
        cuisineTextView.setText(createTextViewForCuisine(cuisine));
        allergensTextView.setText(createTextViewForAllergens(allergens));

        if (image != null) {
            StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(image);
            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(MealView.this).load(uri).into(mealImageView);
            });
        }

        modifyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealView.this, AddMeal.class);
                intent.putExtra("MEAL NAME", name);
                intent.putExtra("PRICE", Double.toString(price));
                intent.putExtra("MEAL TYPE", mealType);
                intent.putExtra("CUISINE", cuisine);
                intent.putExtra("DESCRIPTION", description);
                intent.putExtra("INGREDIENTS", ingredients);
                intent.putExtra("ALLERGENS", allergens);
                intent.putExtra("IMAGE", image);

                startActivity(intent);
                finish();
            }
        });

        removeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseMeal.get().addOnSuccessListener(snapshot -> {
                    Meal thisMeal = snapshot.toObject(Meal.class);
                    if (thisMeal.getOfferStatus()){
                        Toast.makeText(MealView.this, "You cannot remove this meal as it is currently being offered.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                        userRef.collection("meals").document(name).delete();
                        Toast.makeText(MealView.this, "The meal has been successfully removed.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });

        offerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseMeal.get().addOnSuccessListener(snapshot -> {
                    Meal thisMeal = snapshot.toObject(Meal.class);
                    if (offerToggle.isChecked()) {
                        thisMeal.offerMeal();
                        firebaseMeal.update("offered", true);
                        offerToggle.setText("Offered");
                    }
                    else {
                        thisMeal.stopOffering();
                        firebaseMeal.update("offered", false);
                        offerToggle.setText("Not Offered");
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseMeal.get().addOnSuccessListener(documentSnapshot -> {
            if(Boolean.TRUE.equals(documentSnapshot.getBoolean("offered"))) {
                offerToggle.setChecked(true);
                offerToggle.setText("Offered");
            }
            else{
                offerToggle.setChecked(false);
                offerToggle.setText("Not Offered");
            }
        });
    }

    public String createTextViewForIngredient(ArrayList<String> list){
        temp1 = "";
        for(String s: list){
            visibleIngredents = ", " + s;
            temp1 += visibleIngredents;
        }
        return temp1;
    }
    public String createTextViewForAllergens(ArrayList<String> list){
        temp2 = "";
        for(String s: list){
            visibleAllergens = ", " + s;
            temp2 += visibleAllergens;
        }
        return temp2;
    }
    public String createTextViewForCuisine(ArrayList<String> list){
        temp3 = "";
        for(String s: list){
            visibleCuisine = ", " + s;
            temp3 += visibleCuisine;
        }
        return temp3;
    }

}