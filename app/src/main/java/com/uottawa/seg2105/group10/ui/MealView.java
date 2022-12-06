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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.backend.Purchase;
import com.uottawa.seg2105.group10.ui.cookView.Profile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MealView extends AppCompatActivity {

    private static final String TAG = "MEAL_VIEW";
    private DocumentSnapshot document;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    DocumentReference firebaseMeal, userRef;

    String visibleIngredients, visibleCuisine, visibleAllergens, type, cookName, clientName, cookUID, clientUID, pickUpTime;
    private float price;

    Switch offerToggle;
    private TextInputEditText pickupTimeText;
    private TextInputLayout pickupTimeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        String mealName = getIntent().getStringExtra("MEAL NAME");
        cookUID = getIntent().getStringExtra("COOKUID");
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
        Button cookProfileButt = findViewById(R.id.cookProfileButt);
        offerToggle = findViewById(R.id.offerToggle);
        pickupTimeText = (TextInputEditText) findViewById(R.id.pickupTimeEditText);
        pickupTimeLayout = findViewById(R.id.pickupTimeInputLayout);

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
                            clientUID = mAuth.getCurrentUser().getUid();
                            offerToggle.setVisibility(View.GONE);
                            modifyButt.setVisibility(View.GONE);
                            removeButt.setVisibility(View.GONE);
                            break;
                        case "Cook":
                            cookUID = mAuth.getCurrentUser().getUid();
                            purchaseButt.setVisibility(View.GONE);
                            pickupTimeLayout.setVisibility(View.GONE);
                            cookProfileButt.setVisibility(View.GONE);
                            break;

                    }
                }
            }
        });


        firebaseMeal = userRef.collection("meals").document(mealName);

        TextView nameTextView = findViewById(R.id.mealName);
        TextView ingredientTextView = findViewById(R.id.ingredientText);
        TextView cuisineTextView = findViewById(R.id.cuisineText);
        TextView allergensTextView = findViewById(R.id.allergensText);
        TextView meatTypeTextView = findViewById(R.id.mealTypeview);
        TextView priceTextView = findViewById(R.id.mealPrice);
        TextView descriptionTextView = findViewById(R.id.mealDesc);
        ImageView mealImageView = findViewById(R.id.mealImage);

        meatTypeTextView.setText(mealType);
        nameTextView.setText(mealName);

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

        cookProfileButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealView.this, Profile.class);
                userRef = dBase.collection("users").document(cookUID);
                userRef.get().addOnCompleteListener(MealView.this, new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                String email, description, address, cookFirstName, cookLastName;
                                double ratingSum, numReviews;
                                String completedOrders;
                                numReviews = document.getDouble("numReviews");
                                ratingSum = document.getDouble("ratingSum");
                                address = document.getString("address");
                                cookFirstName = document.getString("firstName");
                                cookLastName = document.getString("lastName");
                                email = document.getString("email");
                                description = document.getString("description");
                                completedOrders = String.valueOf(document.get("completedOrders"));
                                intent.putExtra("numReviews", numReviews);
                                intent.putExtra("ratingSum", ratingSum);
                                intent.putExtra("firstName", cookFirstName);
                                intent.putExtra("lastName", cookLastName);
                                intent.putExtra("email", email);
                                intent.putExtra("description", description);
                                intent.putExtra("address", address);
                                intent.putExtra("rating", ratingSum);
                                intent.putExtra("completedOrders", completedOrders );
                                intent.putExtra("type", type);
                                startActivity(intent);
                            }
                        }
                    }
                    });
            }
        });

        modifyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealView.this, AddMeal.class);
                intent.putExtra("MEAL NAME", mealName);
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
                    } else {
                        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                        userRef.collection("meals").document(mealName).delete();
                        Toast.makeText(MealView.this, "The meal has been successfully removed.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
        purchaseButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensures user input is satisfactory before continuing method.
                if (!validateDateTime()) return;

                // fetches the cook's information
                userRef = dBase.collection("users").document(cookUID);
                userRef.get().addOnCompleteListener(cookTask -> {
                    if (cookTask.isSuccessful()) {
                        document = cookTask.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            cookName = document.getString("firstName") + " " + document.getString("lastName");

                            dBase.collection("users").document(clientUID).get().addOnCompleteListener(clientTask -> {
                                if (cookTask.isSuccessful()) {
                                    document = cookTask.getResult();
                                    if (document.exists()) {
                                        clientName = document.getString("firstName") + " " + document.getString("lastName");

                                        String pattern = "yy/MM/dd-HH:mm";
                                        pickUpTime = pickupTimeText.getText().toString();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                                        LocalDateTime pickup = LocalDateTime.from(formatter.parse(pickUpTime));
                                        String docID = System.currentTimeMillis() + "";
                                        try {
                                            Meal.createPurchase(docID, cookUID, clientUID, mealName, image, pickup, clientName, cookName, Purchase.PENDING);
                                            Toast.makeText(MealView.this, "Purchase request submitted!", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(MealView.this, "Purchase request failed to submit.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });




                        }
                    }
                });
                finish();
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
                    } else {
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
            if (Boolean.TRUE.equals(documentSnapshot.getBoolean("offered"))) {
                offerToggle.setChecked(true);
                offerToggle.setText("Offered");
            } else {
                offerToggle.setChecked(false);
                offerToggle.setText("Not Offered");
            }
        });
    }

    public String createTextViewForIngredient(ArrayList<String> list) {
        visibleIngredients = "";
        for (String s : list) {
            if (list.get(0) != s) visibleIngredients += ", ";
            visibleIngredients += s;
        }
        return visibleIngredients;
    }

    public String createTextViewForAllergens(ArrayList<String> list) {
        visibleAllergens = "";
        for (String s : list) {
            if (list.get(0) != s) visibleAllergens += ", ";
            visibleAllergens += s;
        }
        return visibleAllergens;
    }

    public String createTextViewForCuisine(ArrayList<String> list) {
        visibleCuisine = "";
        for (String s : list) {
            if (list.get(0) != s) visibleCuisine += ", ";
            visibleCuisine += s;
        }
        return visibleCuisine;
    }

    // Returns true if expiry-date is of yy/MM/dd-HH:mm format; returns false otherwise.
    public boolean validateDateTime() {
        String val = String.valueOf(pickupTimeText.getText()).trim();

        String pattern = "yy/MM/dd-HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        if (val.isEmpty()) {
            pickupTimeLayout.setError("Field cannot be empty");
            return false;
        }

        try {
            formatter.parse(val);
        } catch (Exception e) {
            pickupTimeLayout.setError("Invalid date and time! Must be of " + pattern + " format.");
            return false;
        }
        pickupTimeLayout.setError(null);
        pickupTimeLayout.setErrorEnabled(false);
        return true;
    }

}