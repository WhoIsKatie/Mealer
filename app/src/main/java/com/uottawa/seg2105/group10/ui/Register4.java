package com.uottawa.seg2105.group10.ui;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.User;

public class Register4 extends AppCompatActivity {

    private static final String TAG = "Register4";

    private Button submitButt;
    private Button login;
    private ImageButton back;
    private Button galleryButt;
    private TextInputEditText profile;
    private ImageView voidCheck;
    private final int GALLERY_REQ_CODE = 1000;

    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);
        User user = Register2.user;

        // Initialize FirebaseAuth and FirebaseStorage objects
        storage = FirebaseStorage.getInstance();

        submitButt = findViewById(R.id.cookSubmitButt);
        login = findViewById(R.id.reg4LoginButt);
        back = findViewById(R.id.reg4BackButt);
        voidCheck = findViewById(R.id.peekChequeImg);
        galleryButt = findViewById(R.id.galleryLaunchButt);

        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = (TextInputEditText) findViewById(R.id.profileDescUpper);
                String profDesc = profile.getText().toString();

                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();

                // TODO: use Cook.java completeProfile method to complete profile

                // https://firebase.google.com/docs/storage/android/upload-files
                // ((Cook) user).completeProfile(profDesc, JAKE PUT STUFF HERE);


                // Add user document with Uid set as document ID to collection of "users" in Firestore
                dBase.collection("users").document(mAuth.getCurrentUser().getUid()).set(user);
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
                Register2.user = null;

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
                Register2.user = null;
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
}