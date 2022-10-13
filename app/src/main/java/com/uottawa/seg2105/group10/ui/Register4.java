package com.uottawa.seg2105.group10.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.User;

public class Register4 extends AppCompatActivity {

    private Button nextButt;
    private Button login;
    private ImageButton back;
    private Button gal;
    private TextInputEditText profile;
    private  ImageView check;
    private final int GALLERY_REQ_CODE = 1000;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);
        User user = Register2.user;
        nextButt = findViewById(R.id.signup_submit_button);
        login = findViewById(R.id.signup_login_button2);
        back = findViewById(R.id.BackButFOrReg4);
        check = findViewById(R.id.check);
        gal = findViewById(R.id.galery);
        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = (TextInputEditText) findViewById(R.id.profileDescUpper);
                String profDesc = profile.getText().toString();


                //((Cook) user).setCC(profDesc, JAKE PUT STUFF HERE);

            }



            //TODO: use Client.java setCC method to complete profile

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

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            if (resultCode == GALLERY_REQ_CODE){
                //For gallery
                check.setImageURI(data.getData());
            }
        }

    }
}