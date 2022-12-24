package com.uottawa.seg2105.group10.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<FirebaseUser> user;

    public LiveData<FirebaseUser> login(String email, String pass) {
        user = new MutableLiveData<>();
        loginFirebase(email, pass);
        return user;
    }

    private void loginFirebase(String email, String pass) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // this is inside onclick so it doesn't run immediately when the activity begins
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Log.d("LoginViewModel", "signInWithEmail:success");
                        user.setValue(mAuth.getCurrentUser());

                    } else
                        Log.w("LoginViewModel", "signInWithEmail:failure", task.getException());
                });
    }

}
