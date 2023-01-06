package com.uottawa.seg2105.group10.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.uottawa.seg2105.group10.Mealer;
import com.uottawa.seg2105.group10.repositories.User;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<User> user;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<User> login(String email, String pass) {
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
                        Mealer app = (Mealer) getApplication().getApplicationContext();
                        app.initializeUser(result -> {
                            Log.d("LoginViewModel", result.getFirstName());
                            user.setValue(result);
                        });
                    } else
                        Log.w("LoginViewModel", "signInWithEmail:failure", task.getException());
                });
    }

}
