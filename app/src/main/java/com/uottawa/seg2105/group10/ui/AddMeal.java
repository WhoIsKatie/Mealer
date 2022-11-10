
package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Utility;

public class AddMeal extends AppCompatActivity {
    private Uri filePath;
    private ImageView mealImage;
    private FirebaseAuth mAuth;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        Button removeButt, save;
        EditText mealName, mealDesc, mealPrice;
        mealImage = findViewById(R.id.mealImage);
        Button changePicture = findViewById(R.id.changePicture);
        mAuth = FirebaseAuth.getInstance();

        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, 1000);
            }
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
}
