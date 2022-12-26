package com.uottawa.seg2105.group10;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.repositories.Admin;
import com.uottawa.seg2105.group10.repositories.Client;
import com.uottawa.seg2105.group10.repositories.Cook;
import com.uottawa.seg2105.group10.repositories.User;

import java.util.Objects;

public class Mealer extends Application {

    private static final String TAG = "Mealer Startup";
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    //private final DocumentSnapshot[] userSnapshot = new DocumentSnapshot[1];
    private String type;
    private MutableLiveData<User> user;
    //private User user;

    /**
     * Called when the application starts - before any other application objects are created.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        user = new MutableLiveData<>();
    }

    /** Called when the device configuration changes while the component is running.
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    /**
     * Called when the overall system is running low on memory.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void initializeUser(final Callback callback) {
        // create reference to current user document
        DocumentReference userRef = dBase.collection("users")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        userRef.get().addOnSuccessListener(snapshot -> {
            Log.d(TAG, "DocumentSnapshot data: " + snapshot.getData());
            type = snapshot.getString("type");
            User result;
            if (Objects.equals(type, "Cook"))
                result = snapshot.toObject(Cook.class);
            else if (Objects.equals(type, "Admin"))
                result = Admin.getInstance();
            else
                result = snapshot.toObject(Client.class);
            callback.firebaseResponseCallback(result);
        });
    }

    public void setUser(User user) {
        if (user == null) return;
        if (Objects.equals(user.getUid(), mAuth.getCurrentUser().getUid()))
            this.user.setValue(user);
    }

    public void getUser(String uid, final Callback callback) {
        DocumentReference userRef = dBase.collection("users").document(uid);

        userRef.get().addOnSuccessListener(snapshot -> {
            Log.d(TAG, "DocumentSnapshot data: " + snapshot.getData());
            User result;
            if (Objects.equals(snapshot.getString("Type"), "Cook"))
                result = snapshot.toObject(Cook.class);
            else if (Objects.equals(snapshot.getString("Type"), "Admin"))
                result = Admin.getInstance();
            else
                result = snapshot.toObject(Client.class);
            callback.firebaseResponseCallback(result);
        });
    }

    public String getType() {
        return type;
    }

    public interface Callback {
        void firebaseResponseCallback(User result);
    }

}
