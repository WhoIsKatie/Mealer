package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.backend.Purchase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MealView extends AppCompatActivity {

    private static final String TAG = "MEAL_VIEW";
    private DocumentSnapshot document;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private float price;
    Timestamp stringPickupTime;
    DocumentReference firebaseMeal, userRef, pruchaseRef, mealRef;
    String visibleIngredients, temp1,  visibleCuisine, temp2,  visibleAllergens, temp3, type, firstName, cookUID, cookref;
    Switch offerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        String name = getIntent().getStringExtra("MEAL NAME");
        price = getIntent().getExtras().getFloat("PRICE");
        String image = getIntent().getStringExtra("IMAGE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        ArrayList<String> ingredients = getIntent().getStringArrayListExtra("INGREDIENTS");
        ArrayList<String> allergens = getIntent().getStringArrayListExtra("ALLERGENS");
        ArrayList<String> cuisine = getIntent().getStringArrayListExtra("CUISINE");
        String mealType = getIntent().getStringExtra("MEAL TYPE");

        BigDecimal bd = new BigDecimal(price + "");
        String textPrice = bd.setScale(2, RoundingMode.HALF_EVEN).toString();

        //Initializing buttons
        Button modifyButt = findViewById(R.id.modifyButt);
        Button removeButt = findViewById(R.id.removeButt);
        Button purchaseButt = findViewById(R.id.purchaseButt);
        offerToggle = findViewById(R.id.offerToggle);

        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
        userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        document = task.getResult();
                        // if user specific document exists,
                        // set text field to display user type (Client, Cook)
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            type = document.getString("type");
                            switch (type) {
                                case "Client":
                                    offerToggle.setVisibility(View.GONE);
                                    modifyButt.setVisibility(View.GONE);
                                    purchaseButt.setVisibility(View.VISIBLE);
                                    break;
                                case "Cook":
                                    offerToggle.setVisibility(View.VISIBLE);
                                    modifyButt.setVisibility(View.VISIBLE);
                                    purchaseButt.setVisibility(View.GONE);
                                    break;

                            }
                        }
                    }
        });




        firebaseMeal = userRef.collection("meals").document(name);

        TextView nameTextView = findViewById(R.id.mealName);
        TextView ingredientTextView = findViewById(R.id.ingredientText);
        TextView cuisineTextView = findViewById(R.id.cuisineText);
        TextView allergensTextView = findViewById(R.id.allergensText);
        TextView meatTypeTextView = findViewById(R.id.mealTypeview);
        TextView priceTextView = findViewById(R.id.mealPrice);
        TextView descriptionTextView = findViewById(R.id.mealDesc);
        ImageView mealImageView = findViewById(R.id.mealImage);

        meatTypeTextView.setText(mealType);
        nameTextView.setText(name);

        priceTextView.setText(textPrice);
        descriptionTextView.setText(description);
        ingredientTextView.setText(createTextViewForIngredient(ingredients));
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
                intent.putExtra("PRICE", price);
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
                    if (Boolean.TRUE.equals(snapshot.getBoolean("offered"))) {
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
        purchaseButt.setOnClickListener(new View.OnClickListener() {
            @Override
            //using multipule listeners becuase you cant reference a feild without looking at the entire document
            public void onClick(View view) {
                //gets the firstname
                userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        document = task.getResult();

                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            firstName = document.getString("firstName");
                        }
                    }
                });
                //get the pickuptime
                pruchaseRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid()).collection("purchases").document(mAuth.getCurrentUser().getUid());
                pruchaseRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        document = task.getResult();

                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            stringPickupTime = document.getTimestamp("pickUpTime");
                        }
                    }
                });
                //gets the cookUID based on the meal chosen to purchase
                mealRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid()).collection("meals").document(name);
                mealRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        document = task.getResult();

                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            cookUID = document.getString("cookUID");
                        }
                    }
                });
                //lines 206-209 are to be able to change stringPickupTime into a localDateTime object
                Date date = new Date();
                Timestamp currentTime = new Timestamp(date);
                String docUID = currentTime.toString();
                LocalDateTime currentLocalTime = LocalDateTime.ofInstant(stringPickupTime.toDate().toInstant(), ZoneId.systemDefault());
                //gets userID and turns it into a string
                String userRefString = userRef.toString();
                //uses the method createPurchase in meal class to create a new purchase
                Purchase newPurchase = Meal.createPurchase(docUID, cookUID, userRefString, name, currentLocalTime, firstName);

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
        visibleIngredients = "";
        for(String s: list){
            if (list.get(0) != s) visibleIngredients += ", ";
            visibleIngredients += s;
        }
        return visibleIngredients;
    }
    public String createTextViewForAllergens(ArrayList<String> list){
        visibleAllergens = "";
        for(String s: list){
            if (list.get(0) != s) visibleAllergens += ", ";
            visibleAllergens += s;
        }
        return visibleAllergens;
    }
    public String createTextViewForCuisine(ArrayList<String> list){
        visibleCuisine = "";
        for(String s: list){
            if (list.get(0) != s) visibleCuisine += ", ";
            visibleCuisine += s;
        }
        return visibleCuisine;
    }

}