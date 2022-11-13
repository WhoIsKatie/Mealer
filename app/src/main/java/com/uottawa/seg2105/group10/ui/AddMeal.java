
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.backend.Utility;

import java.util.HashSet;

public class AddMeal extends AppCompatActivity {
    private Uri filePath;
    private ImageView mealImage;
    private Button confirmButt, addIngredientButt, mealTypeButt, addAllergenButt, cuisineTypeButt;
    private ChipGroup ingredientsChipGroup, allergensChipGroup;
    private EditText mealName, mealPrice, mealDesc;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore dBase;
    DocumentReference FirebaseMeal, userRef;

    // Assuming we'll be using a multi-selection list/combo box that accepts user input as values
    private HashSet<String> ingredients;
    private HashSet<String> allergies;
    private String imageID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        dBase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // initializing the edit texts and buttons
        Button changePicture = findViewById(R.id.changePicture);
        confirmButt = findViewById(R.id.confirmButt);
        mealImage = findViewById(R.id.mealImage);
        mealName = findViewById(R.id.mealName);
        mealDesc = findViewById(R.id.mealDesc);
        mealPrice = findViewById(R.id.mealPrice);
        mealDesc = findViewById(R.id.mealDesc);
        addIngredientButt = findViewById(R.id.addIngredientButt);
        addAllergenButt = findViewById(R.id.addAllergenButt);

        // setting up things to get the ID of the meal we want to update
        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());//this is so we can add something to the collection first, get its ID, then update later
        FirebaseMeal = userRef.collection("meals").add(new Meal(0)).getResult(); //todo: all cooks need subcollection meals in reg4

        // we need to fetch all the chips inside the chip group and populate the hashsets
        // since it must be done multiple times I'll make a method in Utility: we can move it later if it doesnt make sense to be in utility
        ingredients = Utility.expandChipGroup(ingredientsChipGroup);
        allergies = Utility.expandChipGroup(allergensChipGroup);


        changePicture.setOnClickListener(view -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, 1000);
        });

        addIngredientButt.setOnClickListener(view -> {
            // not done
        });

        addAllergenButt.setOnClickListener(view -> {
            // not done
        });

        confirmButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fetch the text fields
                if(! validateMealName()&&validatePrice()&&validateDescription()&&validateAllergies()&&validateIngredients()){
                    return;
                }
                String name = mealName.getText().toString();
                float price = Float.parseFloat(mealPrice.getText().toString());
                String docID = FirebaseMeal.getId(); //get the ID so we can update the meal there
                String mealType = mealTypeButt.getText().toString();
                String cuisine = cuisineTypeButt.getText().toString();
                String description = mealDesc.getText().toString();

                Meal mealToAdd = new Meal(docID, price, name, description, mealType, cuisine, ingredients, allergies, imageID);
                FirebaseMeal.set(mealToAdd).addOnFailureListener(e -> {
                    Toast.makeText(AddMeal.this, "Could not add the meal.", Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(unused -> {
                    //todo: here we can add to our recycler view
                    Toast.makeText(AddMeal.this, "Added meal!", Toast.LENGTH_SHORT).show();
                    finish();
                });
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
            // GOING TO TRY UPLOADING TO A SUB-COLLECTION FOLDER FOR MEAL PICS FOR THE SPECIFIC COOK
            Utility util = new Utility(AddMeal.this, filePath, mAuth, userRef);
            // todo: if this is the implementation we go with, need to update register4 to add sub-collection for all cooks called mealImages
            imageID = util.uploadToSubcollection();
            FirebaseMeal.update("imageID", imageID);
        }
    }


    // Meal Helper Methods -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

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

        double price = Integer.parseInt(val);
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

    private boolean validateAllergies() {
        //TODO: FIX THESE VALIDATIONS BROKEN BY CHANGING mealAllergies to chipgroup
        /*if (allergies.isEmpty()) {
            mealAllergies.setError("Field cannot be empty");
            return false;
        }
        mealAllergies.setError(null);*/
        return true;
    }

    private boolean validateIngredients(){
        return true;    // todo: to fill later
    }

}
