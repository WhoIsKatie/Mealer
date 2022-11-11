
package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Utility;

import java.util.HashSet;

public class AddMeal extends AppCompatActivity {
    private Uri filePath;
    private ImageView mealImage;
    private Button confirmButt;
    private EditText mealName, mealPrice, mealDesc, mealAllergies;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private final FirebaseStorage dBase = FirebaseStorage.getInstance();

    // Assuming we'll be using a multi-selection list/combo box that accepts user input as values
    private HashSet<String> descr;
    private HashSet<String> allergies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        // initializing the edit texts and buttons
        confirmButt = findViewById(R.id.confirmButt);
        mealImage = findViewById(R.id.mealImage);
        mealName = findViewById(R.id.mealName);
        mealDesc = findViewById(R.id.mealDesc);
        mealPrice = findViewById(R.id.mealPrice);
        //mealAllergies = findViewById(R.id.mealAllergies);
        Button changePicture = findViewById(R.id.changePicture);
        mAuth = FirebaseAuth.getInstance();

        changePicture.setOnClickListener(view -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, 1000);
        });

        confirmButt.setOnClickListener(view -> {
            //TODO FOR KATIE: I WILL FETCH FROM TEXT FIELD BUT U NEED TO CHECK THEM :)
            dBase.getReference();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 1000){
                filePath = data.getData();
                mealImage.setImageURI(filePath);
            }
            Utility util = new Utility(AddMeal.this, filePath, mAuth, storage);
            util.uploadImage();
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
        if (descr.isEmpty()) {
            mealDesc.setError("Field cannot be empty");
            return false;
        }
        mealDesc.setError(null);
        return true;
    }

    private boolean validateAllergies() {
        if (allergies.isEmpty()) {
            mealAllergies.setError("Field cannot be empty");
            return false;
        }
        mealAllergies.setError(null);
        return true;
    }

}
