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
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.User;

public class Register4 extends AppCompatActivity {

    private Button submitButt;
    private Button login;
    private ImageButton back;
    private Button galleryButt;
    private TextInputEditText profile;
    private ImageView voidCheck;
    private final int GALLERY_REQ_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);
        User user = Register2.user;
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

                //TODO: use Client.java setCC method to complete profile
                //((Cook) user).setCC(profDesc, JAKE PUT STUFF HERE);

            }


        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register4.this, Login.class));
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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