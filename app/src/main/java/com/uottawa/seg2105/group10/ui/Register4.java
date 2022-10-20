package com.uottawa.seg2105.group10.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Cook;

import java.util.Map;
import java.util.UUID;

public class Register4 extends AppCompatActivity {

    private static final String TAG = "Register4(Cooks)";

    private Button submitButt;
    private Button login;
    private ImageButton back;
    private Button galleryButt;
    private TextInputEditText profile;
    private ImageView voidCheque;
    private final int GALLERY_REQ_CODE = 1000;

    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private FirebaseStorage storage;
    private Cook user;

    TextInputLayout descriptionField;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);

        // Initialize FirebaseAuth and FirebaseStorage objects
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        submitButt = findViewById(R.id.cookSubmitButt);
        login = findViewById(R.id.reg4LoginButt);
        back = findViewById(R.id.reg4BackButt);
        voidCheque = findViewById(R.id.peekChequeImg);
        galleryButt = findViewById(R.id.galleryLaunchButt);
        descriptionField = findViewById(R.id.profileDescLayout);

        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = (TextInputEditText) findViewById(R.id.profileDescUpper);
                String profDesc = profile.getText().toString();

                if(!validateDescription()) {
                    return;
                }

                // TODO: use Cook.java completeProfile method to complete profile

                // https://firebase.google.com/docs/storage/android/upload-files
                // ((Cook) user).completeProfile(profDesc, JAKE PUT STUFF HERE);

                // Add user document with Uid set as document ID to collection of "users" in Firestore
                DocumentReference userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                // Set the "description" field of the cook
                Map<String, String> data = Register2.data;
                data.put("description", profDesc);
                data.put("type", "Cook");
                userRef.set(data);
                user = new Cook(userRef);
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
                //TODO: Jake, please replace startActivityForResult method (open link below)
                //https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
            }
        });

    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQ_CODE){
                filePath = data.getData();
                voidCheque.setImageURI(filePath);

            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storage.getReference().child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            /* TODO: this does literally nothing but also no errors so i pushed it haha*/
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(Register4.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {// Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(Register4.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
}