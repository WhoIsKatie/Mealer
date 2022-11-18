package com.uottawa.seg2105.group10.ui;

import android.annotation.SuppressLint;
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
                intent.putExtra("Ingredients", ingredients);
                intent.putExtra("Allergens", allergens);
                intent.putExtra("Cuisine", cuisine);
                intent.putExtra("MealType", mealType);


                startActivity(intent);
                finish();
            }
        });

        removeButt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View view) {
                firebaseMeal.get().addOnSuccessListener(snapshot -> {
                    Meal thisMeal = snapshot.toObject(Meal.class);
                    if (thisMeal.isOffered()){
                        Toast.makeText(MealView.this, "You cannot remove this meal as it is currently being offered.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                        //System.out.println(userRef.collection("meals").document(mAuth.getCurrentUser().getUid()));
                        userRef.collection("meals").document(name).delete();
                        Toast.makeText(MealView.this, "The meal has been successfully removed.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                });

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


    public String createTextViewForIngredient(ArrayList<String> list){
        temp1 = "";
        for(String s: list){
            visibleIngredents = s + ", ";
            temp1 += visibleIngredents;
        }
        return temp1;
    }
    public String createTextViewForAllergens(ArrayList<String> list){
        temp2 = "";
        for(String s: list){
            visibleAllergens = s + ", ";
            temp2 += visibleAllergens;
        }
        return temp2;
    }
    public String createTextViewForCuisine(ArrayList<String> list){
        temp3 = "";
        for(String s: list){
            visibleCuisine = s + ", ";
            temp3 += visibleCuisine;
        }
        return temp3;
    }

}