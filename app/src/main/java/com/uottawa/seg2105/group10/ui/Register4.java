package com.uottawa.seg2105.group10.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Cook;
import com.uottawa.seg2105.group10.backend.User;
import com.uottawa.seg2105.group10.backend.Utility;

import java.util.Map;

public class Register4 extends AppCompatActivity {

    private static final String TAG = "Register4 (Cooks)";

    private TextInputEditText profile;
    private ImageView voidCheque;
    private final int GALLERY_REQ_CODE = 1000;

    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private User user;

    TextInputLayout descriptionField;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);

        // Initialize FirebaseAuth and FirebaseStorage objects
        FirebaseStorage storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        Button submitButt = findViewById(R.id.cookSubmitButt);
        Button login = findViewById(R.id.reg4LoginButt);
        ImageButton back = findViewById(R.id.reg4BackButt);
        voidCheque = findViewById(R.id.peekChequeImg);
        Button galleryButt = findViewById(R.id.galleryLaunchButt);
        descriptionField = findViewById(R.id.profileDescLayout);


        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = (TextInputEditText) findViewById(R.id.profileDescUpper);
                String profDesc = profile.getText().toString();
                if(!validateDescription()) {
                    return;
                }

                // Add user document with Uid set as document ID to collection of "users" in Firestore
                DocumentReference userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                // Set the "description" field of the cook
                Map<String, String> data = Register2.data;

                data.put("description", profDesc);
                data.put("type", "Cook");
                Utility util = new Utility(Register4.this, filePath, mAuth, storage);
                data.put("cheque", util.uploadImage("cheques/" + mAuth.getUid() + "/"));

                user = new Cook(data);
                userRef.set(user);
                //userRef.set(data);
                //userRef.update("isSuspended", false);
                //user = new Cook(userRef);
                // adding a sub-collection to user document to keep Mealer User object and DateTime suspensionEnd
                //userRef.collection("userObject").document("Cook").set(user);
                //userRef.update("meals", null);

                // Redirects user to login activity

                startActivity(new Intent(Register4.this, Login.class));
            }


        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete incomplete user data when user leaves register activity WITHOUT
                // completing registration activities
                dBase.collection("users").document(mAuth.getCurrentUser().getUid()).delete();
                mAuth.getCurrentUser().delete();
                user = null;

                // Redirects user to login activity WITHOUT completing registration activities
                startActivity(new Intent(Register4.this, Login.class));
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete incomplete user data when user leaves register activity WITHOUT
                // completing registration activities
                dBase.collection("users").document(mAuth.getCurrentUser().getUid()).delete();
                mAuth.getCurrentUser().delete();
                user = null;
                finish();
            }

        });

        galleryButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQ_CODE){
                filePath = data.getData();
                voidCheque.setImageURI(filePath);
            }
        }
    }


    // Description Helper Method
    private boolean validateDescription(){
        String val = descriptionField.getEditText().getText().toString().trim();

        if(val.isEmpty()) {
            descriptionField.setError("Field can not be empty");
            return false;
        }
        if(val.length() > 500 ){
            descriptionField.setError("Field must not go over 500 characters");
            return false;
        }
        else{
            descriptionField.setError(null);
            descriptionField.setErrorEnabled(false);
            return true;
        }
    }


}