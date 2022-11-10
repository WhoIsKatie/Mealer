package com.uottawa.seg2105.group10.backend;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Utility{
    private Uri filePath;
    private final FirebaseAuth mAuth;
    private final Context context;
    private final FirebaseStorage storage;

    public Utility(Context context, Uri filePath, FirebaseAuth mAuth, FirebaseStorage storage){
        this.filePath = filePath;
        this.mAuth = mAuth;
        this.context = context;
        this.storage = storage;
    }

    public void setURI(ImageView imageView, int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 1000){
                filePath = data.getData();
                imageView.setImageURI(filePath);
            }
        }
    }

    public void uploadImage()
    {
        if (filePath != null) {

            // Defining the child of storageReference
            StorageReference imageRef = storage.getReference().child(mAuth.getUid() + "/cheque/" + filePath.getLastPathSegment());

            // Register observers to listen for when the download is done or if it fails
            imageRef.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully
                // Dismiss dialog
                Toast.makeText(context, "Upload success!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
