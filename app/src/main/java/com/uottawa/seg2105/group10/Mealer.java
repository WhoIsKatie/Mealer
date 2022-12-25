package com.uottawa.seg2105.group10;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.repositories.Client;
import com.uottawa.seg2105.group10.repositories.Cook;
import com.uottawa.seg2105.group10.repositories.User;

import java.util.Objects;

public class Mealer extends Application {

    private static final String TAG = "Mealer Startup";
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore dBase;
    private static final DocumentSnapshot[] userSnapshot = new DocumentSnapshot[1];
    private static String type;
    private static final User[] user = new User[1];

    /**
     * Called when the application starts - before any other application objects are created.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        if (getUser() != null) type = user[0].getType();
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

    public void initializeUser() {
        // create reference to current user document
        DocumentReference userDoc = dBase.collection("users")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        userDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userSnapshot[0] = task.getResult();

                if (userSnapshot[0].exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + userSnapshot[0].getData());
                    type = userSnapshot[0].getString("type");
                    if (type == "Cook") {
                        user[0] = userSnapshot[0].toObject(Cook.class);
                    } else if (type == "Client") {
                        user[0] = userSnapshot[0].toObject(Client.class);
                    }
                }
            }
        });
    }

    public User getUser() {
        if (mAuth.getCurrentUser() != null)
            initializeUser();
        return user[0];
    }

    public String getType() {
        return type;
    }

}
