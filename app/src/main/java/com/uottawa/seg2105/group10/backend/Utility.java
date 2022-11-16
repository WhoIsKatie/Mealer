package com.uottawa.seg2105.group10.backend;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.HashSet;

public class Utility{
    private Uri filePath;
    private final FirebaseAuth mAuth;
    private final Context context;
    private FirebaseStorage storage = null;
    private DocumentReference doc = null;


    public Utility(Context context, Uri filePath, FirebaseAuth mAuth, FirebaseStorage storage){
        this.filePath = filePath;
        this.mAuth = mAuth;
        this.context = context;
        this.storage = storage;
    }

    public Utility(Context context, Uri filePath, FirebaseAuth mAuth, DocumentReference doc){
        this.filePath = filePath;
        this.mAuth = mAuth;
        this.context = context;
        this.doc = doc;
    }

    @Deprecated
    public void setURI(ImageView imageView, int requestCode, int resultCode, @Nullable Intent data) {
        //TODO: FIGURE OUT IF THIS METHOD IS NECESSARY? HOW TO USE IT?
        if(resultCode == RESULT_OK){
            if(requestCode == 1000){
                filePath = data.getData();
                imageView.setImageURI(filePath);
            }
        }
    }

    public String uploadImage(String path)
    {
        String result = null;
        if (filePath != null && doc == null) {
            // Defining the child of storageReference
            StorageReference imageRef = storage.getReference().child(path + "_" +filePath.getLastPathSegment());
            result = imageRef.getPath();
            // Register observers to listen for when the download is done or if it fails
            imageRef.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully
                // Dismiss dialog
                Toast.makeText(context, "Upload success!", Toast.LENGTH_SHORT).show();
            });
        }
        else{
            // I feel like toasts are more for users. Since this message is exclusively for us, it can just be printed thru system output - Katie
            Toast.makeText(context, "Either filePath is null or FirebaseStorage not Initialized (wrong constructor)", Toast.LENGTH_LONG).show();
            System.out.println("Either filePath is null or FirebaseStorage not Initialized (wrong constructor)");
        }
        return result;
    }

    public static HashMap<String, String> expandChipGroup(ChipGroup chipGroup){
        // https://stackoverflow.com/questions/58224630/how-to-get-selected-chips-from-chipgroup
        HashMap<String, String> result = new HashMap<>();
        for(int i = 0; i < chipGroup.getChildCount(); i++){
            Chip chip = (Chip) chipGroup.getChildAt(i);
            String chipText = chip.getText().toString();
            result.put(chipText, chipText);
        }
        return result;
    }
}
