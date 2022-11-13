package com.uottawa.seg2105.group10.backend;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    public void setURI(ImageView imageView, int requestCode, int resultCode, @Nullable Intent data) {
        //TODO: FIGURE OUT IF THIS METHOD IS NECESSARY? HOW TO USE IT?
        if(resultCode == RESULT_OK){
            if(requestCode == 1000){
                filePath = data.getData();
                imageView.setImageURI(filePath);
            }
        }
    }

    public void uploadImage()
    {
        if (filePath != null && doc == null) {

            // Defining the child of storageReference
            StorageReference imageRef = storage.getReference().child(mAuth.getUid() + "/cheque/" + filePath.getLastPathSegment());

            // Register observers to listen for when the download is done or if it fails
            imageRef.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully
                // Dismiss dialog
                Toast.makeText(context, "Upload success!", Toast.LENGTH_SHORT).show();
            });
        }
        else{
            Toast.makeText(context, "Either filePath is null or FirebaseStorage not Initialized (wrong constructor)", Toast.LENGTH_LONG).show();
        }
    }

    public String uploadToSubcollection(){ //change return type to string if we can get the imageID
        final String[] ID = new String[1];
        if(filePath != null){
            doc.collection("mealImages").add(filePath).addOnSuccessListener(documentReference -> {
                Toast.makeText(context, "Upload success!", Toast.LENGTH_SHORT).show();
                // return doc.collection("mealImages").document()
                // TODO: FIND A WAY TO GET IMAGEID OR SOMETHING SIMILAR FROM HERE?
                ID[0] = documentReference.getId();
            });
        }
        return ID[0];
    }

    public static HashSet<String> expandChipGroup(ChipGroup chipGroup){
        // https://stackoverflow.com/questions/58224630/how-to-get-selected-chips-from-chipgroup
        HashSet<String> result = new HashSet<>();
        for(int i = 0; i < chipGroup.getChildCount(); i++){
            Chip chip = (Chip) chipGroup.getChildAt(i);
            result.add(chip.getText().toString());
        }
        return result;
    }
}
