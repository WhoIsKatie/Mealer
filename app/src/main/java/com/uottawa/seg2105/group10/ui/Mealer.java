package com.uottawa.seg2105.group10.ui;

import android.app.Application;
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
    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();

        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser() != null) {
            //TODO: send user to specified activity
            initializeUser();

        }

    }

    public static void initializeUser() {
        // create reference to current user document
        DocumentReference userDoc = dBase.collection("users").document(mAuth.getCurrentUser().getUid());

        userDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userSnapshot[0] = task.getResult();

                if (userSnapshot[0].exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + userSnapshot[0].getData());
                    type = userSnapshot[0].getString("type");
                    if (type == "Cook") {
                        user = new Cook((String) userSnapshot[0].get("firstName"),
                                (String) userSnapshot[0].get("lastName"),
                                (String) userSnapshot[0].get("email"),
                                (String) userSnapshot[0].get("password"),
                                (String) userSnapshot[0].get("address"),
                                (String) userSnapshot[0].get("description"),
                                (String) userSnapshot[0].get("cheque"),
                                Integer.parseInt(String.valueOf(userSnapshot[0].get("completedOrders"))),
                                Integer.parseInt(String.valueOf(userSnapshot[0].get("numReviews"))),
                                Objects.requireNonNull(userSnapshot[0].getDouble("ratingSum")),
                                mAuth.getCurrentUser().getUid());
                    } else if (type == "Client") {
                        user = new Client((String) userSnapshot[0].get("firstName"),
                                (String) userSnapshot[0].get("lastName"),
                                (String) userSnapshot[0].get("email"),
                                (String) userSnapshot[0].get("password"),
                                (String) userSnapshot[0].get("ccNumber"),
                                (String) userSnapshot[0].get("ccHolderName"),
                                (String) userSnapshot[0].get("expiryDate"),
                                (String) userSnapshot[0].get("cvc"),
                                mAuth.getCurrentUser().getUid());
                    }
                }
            }
        });
    }
}
