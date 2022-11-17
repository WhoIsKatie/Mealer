
package com.uottawa.seg2105.group10.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.backend.Utility;

import java.util.ArrayList;

public class AddMeal extends AppCompatActivity {
    private Uri filePath;
    private ImageView mealImage;
    private ChipGroup mealTypeChipGroup, cuisineChipGroup;
    private EditText mealName, mealPrice, mealDesc, ingredientEditText, allergenEditText;
    private Button mealTypeButt, cuisineTypeButt;
    private View divider;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    DocumentReference firebaseMeal, userRef;
    private TextView showIngredients, showAllergens;
    private String visibleIngredients, visibleAllergens, temp, temp2, currentMealName, currentMealPrice, currentMealDescription;


    // Assuming we'll be using a multi-selection list/combo box that accepts user input as values
    private ArrayList<String> ingredients;
    private ArrayList<String> allergies;
    private String imageID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        // initializing the edit texts and buttons
        Button changePicture = findViewById(R.id.changePicture);
        Button confirmButt = findViewById(R.id.confirmButt);
        Button updateButt = findViewById(R.id.updateButt);
        mealTypeButt = findViewById(R.id.mealTypeButt);
        cuisineTypeButt = findViewById(R.id.cuisineTypeButt);
        mealImage = findViewById(R.id.mealImage);
        mealName = findViewById(R.id.mealName);
        mealDesc = findViewById(R.id.mealDesc);
        mealPrice = findViewById(R.id.mealPrice);
        mealDesc = findViewById(R.id.mealDesc);
        ingredientEditText = findViewById(R.id.ingredientEditText);
        allergenEditText = findViewById(R.id.allergenEditText);
        Button addIngredientButt = findViewById(R.id.addIngredientButt);
        Button addAllergenButt = findViewById(R.id.addAllergenButt);
        mealTypeChipGroup = findViewById(R.id.mealTypeChipGroup);
        cuisineChipGroup = findViewById(R.id.cuisineChipGroup);
        divider = findViewById(R.id.divider22);
        showIngredients = findViewById(R.id.showIngredients);
        showAllergens = findViewById(R.id.showAllergens);

        // setting up things to get the ID of the meal we want to update
        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());//this is so we can add something to the collection first, get its ID, then update later

        // setting up the hash sets
        ingredients = new ArrayList<>();
        allergies = new ArrayList<>();

        visibleIngredients = " ";
        visibleAllergens = " ";
        temp = " ";
        changePicture.setOnClickListener(view -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, 1000);
        });

        if(getIntent().getExtras() != null){
                currentMealName = getIntent().getExtras().getString("Name");
                mealName.setText(currentMealName);
                currentMealPrice = getIntent().getExtras().getString("Price");
                mealPrice.setText(currentMealPrice);
                currentMealDescription = getIntent().getExtras().getString("Description");
                mealDesc.setText(currentMealDescription);
                updateButt.setVisibility(View.VISIBLE);
                confirmButt.setVisibility(View.GONE);
        }

        addIngredientButt.setOnClickListener(view -> {
            // validating ingredient(s)
            if (!validateIndividualIngr(ingredientEditText.getText().toString())) return;
            String[] inputIngredients = ingredientEditText.getText().toString().split(",");   // get everything inside the field
            if(!validateIngredients(inputIngredients)) return;

            for(String s : inputIngredients){
                if ((ingredients.size() < 1) && !validateIndividualIngr(s)) return;
                if (allergies.size() >= 30) break;
                ingredients.add(s);
            }
            ingredientEditText.setText("");
            updateIngredientBox();
            Toast.makeText(this, "Ingredient adding succeeded!", Toast.LENGTH_SHORT).show();
        });

        addAllergenButt.setOnClickListener(view -> {
            // validating ingredient(s)
            if (!validateIndividualAllergen(allergenEditText.getText().toString())) return;
            String[] inputAllergens = allergenEditText.getText().toString().split(",");       // storing user input for allergens
            if (!validateAllergies(inputAllergens)) return;

            for(String a : inputAllergens) {
                if (!validateIndividualAllergen(a)) break;
                allergies.add(a);
            }
            allergenEditText.setText("");
            updateAllergiesBox();
            Toast.makeText(this, "Allergen adding succeeded!", Toast.LENGTH_SHORT).show();
        });

        updateButt.setOnClickListener(view -> {
            // This method is the same as the confirmButt as the set() method should update the document instead of creating a new one
            /* if(validateMealName()&&validatePrice()&&validateDescription()&&validateAllergenMap()&&validateIngredientMap()) {
                String name = mealName.getText().toString();
                float price = Float.parseFloat(mealPrice.getText().toString());
                ArrayList<String> cuisine = new ArrayList<>();

                // turn the selected chips into data we can store
                Chip mealChip = mealTypeChipGroup.findViewById(mealTypeChipGroup.getCheckedChipId());
                String mealType = mealChip.getText().toString();

                for(int chipID : cuisineChipGroup.getCheckedChipIds()){
                    Chip cuisineChip = cuisineChipGroup.findViewById(chipID);
                    cuisine.add(cuisineChip.getText().toString());
                }
                String description = mealDesc.getText().toString();

                firebaseMeal = userRef.collection("meals").document(name);
                firebaseMeal.update("mealName", name);
                firebaseMeal.update("price", price);
                firebaseMeal.update("description", description);
                Utility util = new Utility(AddMeal.this, filePath, mAuth, FirebaseStorage.getInstance());
                imageID = util.uploadImage("mealImages/" + mAuth.getUid() + "/");
                firebaseMeal.update("imageID", imageID);
                */
            if(validateMealName()&&validatePrice()&&validateDescription()&&validateIngredientMap()){
                String name = mealName.getText().toString();
                float price = Float.parseFloat(mealPrice.getText().toString());
                ArrayList<String> cuisine = new ArrayList<>();

                // turn the selected chips into data we can store
                Chip mealChip = mealTypeChipGroup.findViewById(mealTypeChipGroup.getCheckedChipId());
                String mealType = mealChip.getText().toString();

                for(int chipID : cuisineChipGroup.getCheckedChipIds()){
                    Chip cuisineChip = cuisineChipGroup.findViewById(chipID);
                    cuisine.add(cuisineChip.getText().toString());
                }
                String description = mealDesc.getText().toString();

                firebaseMeal = userRef.collection("meals").document(name);
                Meal mealToAdd = new Meal(price, name, description, mealType, cuisine, ingredients, allergies);
                Utility util = new Utility(AddMeal.this, filePath, mAuth, FirebaseStorage.getInstance());
                imageID = util.uploadImage("mealImages/" + mAuth.getUid() + "/");
                mealToAdd.setImageID(imageID);

                firebaseMeal.set(mealToAdd).addOnFailureListener(e -> {
                    Toast.makeText(AddMeal.this, "Could not add the meal.", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnSuccessListener(unused -> {
                    // here we can add to our recycler view
                    // it's actually not necessary since starting the recycler view SHOULD automatically
                    // re-query all meals in database again (like complaint view -> AdminHome) - katie :3
                    Toast.makeText(AddMeal.this, "Added meal!", Toast.LENGTH_SHORT).show();
                    finish();
                });
                //TODO: https://firebase.google.com/docs/firestore/manage-data/add-data#java_16 set method suggests that it should overwrite
                // the data of the existing document but it actually creates a new document
            }
        });

        confirmButt.setOnClickListener(view -> {
            //TODO: fetch the ingredient + allergy fields

            // Validating fields before adding meal!
            if (validateMealName() & validatePrice() & validateDescription()
                    & validateIngredientMap() & validateMealType() & validateCuisineTypes()) {
                String name = mealName.getText().toString();
                float price = Float.parseFloat(mealPrice.getText().toString());
                ArrayList<String> cuisine = new ArrayList<>();

                Chip mealChip = mealTypeChipGroup.findViewById(mealTypeChipGroup.getCheckedChipId());
                String mealType = mealChip.getText().toString();                                    // turn the selected chips into String data

                for(int chipID : cuisineChipGroup.getCheckedChipIds()){
                    Chip cuisineChip = cuisineChipGroup.findViewById(chipID);
                    cuisine.add(cuisineChip.getText().toString());
                }

                String description = mealDesc.getText().toString();

                firebaseMeal = userRef.collection("meals").document(name);
                Meal mealToAdd = new Meal(price, name, description, mealType, cuisine, ingredients, allergies);
                Utility util = new Utility(AddMeal.this, filePath, mAuth, FirebaseStorage.getInstance());
                imageID = util.uploadImage("mealImages/" + mAuth.getUid() + "/");
                mealToAdd.setImageID(imageID);

                firebaseMeal.set(mealToAdd).addOnFailureListener(e -> {
                    Toast.makeText(AddMeal.this, "Could not add the meal.", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnSuccessListener(unused -> {
                    Toast.makeText(AddMeal.this, "Added meal!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });

        mealTypeButt.setOnClickListener(view -> {
            if (mealTypeChipGroup.getVisibility() != View.GONE) {
                mealTypeChipGroup.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            } else {
                mealTypeChipGroup.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
            }
        });

        cuisineTypeButt.setOnClickListener(view -> {
            if (cuisineChipGroup.getVisibility() != View.GONE) {
                cuisineChipGroup.setVisibility(View.GONE);
            }
            else {
                cuisineChipGroup.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1000){
                assert data != null;
                filePath = data.getData();
                mealImage.setImageURI(filePath);
            }
        }
    }

    //method to update ingredient text-box
    private void updateIngredientBox(){
        for(String s: this.ingredients){
            temp = visibleIngredients;
            visibleIngredients = " ";
            visibleIngredients = s + ", ";
            temp += visibleIngredients;
        }

        showIngredients.setText("Ingredients: " + temp);
    }
    // method to update Allergen box
    private void updateAllergiesBox(){
        for(String s: this.allergies){
            temp = visibleAllergens;
            visibleAllergens = " ";
            visibleAllergens = s + ", ";
            temp += visibleAllergens;
        }
        showAllergens.setText("Allergens: " + temp);

    }

    // Meal Validation Helper Methods -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    // Returns true if meal name length is 1-30 characters; returns false otherwise.
    private boolean validateMealName(){
        String name = mealName.getText().toString().trim();

        if(name.isEmpty()) {
            mealName.setError("Field cannot be empty");
            return false;
        }
        if(name.length() > 30 ){
            mealName.setError("Field must not go over 30 characters");
            return false;
        }
        else{
            mealName.setError(null);
            return true;
        }
    }

    // Returns true if card number only contains 16 integers; returns false otherwise.
    private boolean validatePrice(){
        String val = mealPrice.getText().toString().trim();
        if(val.isEmpty()) {
            mealPrice.setError("Field cannot be empty");
            return false;
        }

        double price = Double.parseDouble(val);
        if (price < 0) {
            mealPrice.setError("Price must be at least $0.00!");
            return false;
        } else if (price >= 1000) {
            mealPrice.setError("Price must be under $1000.00");
            return false;
        }

        mealPrice.setError(null);
        return true;
    }

    private boolean validateDescription() {
        if (mealDesc.getText().toString().isEmpty()) {
            mealDesc.setError("Field cannot be empty");
            return false;
        }
        mealDesc.setError(null);
        return true;
    }

    private boolean validateAllergies(String[] inputAllergens) {
        if(inputAllergens.length == 0){
            allergenEditText.setError("Field cannot be empty");
            return false;
        }
        allergenEditText.setError(null);
        return true;
    }

    private boolean validateIngredients(String[] inputIngredients){
        if(inputIngredients.length == 0){
            ingredientEditText.setError("Field cannot be empty");
            return false;
        }
        ingredientEditText.setError(null);
        return true;
    }

    private boolean validateIndividualIngr(String ingredient) {
        if (ingredients.size() >= 30) {
            ingredientEditText.setError("Reached maximum ingredient count.");
            ingredientEditText.setText("");
            return false;
        }

        if(ingredientEditText.getText().toString().trim().isEmpty()){
            ingredientEditText.setError("Field cannot be empty!");
            ingredientEditText.setText("");
            return false;
        }

        ingredientEditText.setError(null);
        return true;
    }

    private boolean validateIndividualAllergen(String allergen) {
        if (allergies.size() >= 20) {
            allergenEditText.setError("Reached maximum allergy count.");
            allergenEditText.setText("");
            return false;
        }

        if(allergenEditText.getText().toString().trim().isEmpty()){
            allergenEditText.setError("Field cannot be empty!");
            allergenEditText.setText("");
            return false;
        }

        allergenEditText.setError(null);
        return true;
    }

    private boolean validateIngredientMap(){
        if (ingredients.isEmpty()) {
            ingredientEditText.setError("Must have at least one (1) ingredient listed.");
            return false;
        }
        ingredientEditText.setError(null);
        return true;
    }

    // Ensures that at least one (1) meal-type chip has been checked.
    private boolean validateMealType() {
        if (mealTypeChipGroup.getCheckedChipId() == View.NO_ID) {
            mealTypeButt.setError("Must select at least one choice.");
            return false;
        }
        mealTypeButt.setError(null);
        return true;
    }

    // Ensures that at least one (1) cuisine-type chip has been checked.
    private boolean validateCuisineTypes() {
        if (cuisineChipGroup.getCheckedChipIds().isEmpty()) {
            cuisineTypeButt.setError("Must select at least one choice.");
            return false;
        }
        cuisineTypeButt.setError(null);
        return true;
    }

}
