package com.uottawa.seg2105.group10.ui.recyclers;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.uottawa.seg2105.group10.Mealer;
import com.uottawa.seg2105.group10.repositories.Purchase;
import com.uottawa.seg2105.group10.repositories.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PurchasesViewModel extends AndroidViewModel {

    private static final String TAG = "ClientHomeViewModel";
    private MutableLiveData<List<Purchase>> purchases;
    private MutableLiveData<User> currentUser, cookUser;

    public PurchasesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Purchase>> getPurchases(String type) {
        purchases = new MutableLiveData<>();
        loadPurchasesFromFirebase(type);
        return purchases;
    }

    public void loadPurchasesFromFirebase(String type) {
        FirebaseFirestore dBase = FirebaseFirestore.getInstance();
        ArrayList<Purchase> purchaseList = new ArrayList<>();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Query purchaseQuery;
        if (type.equals("Client"))
            purchaseQuery = dBase.collection("purchases").whereEqualTo("clientUID", uid);
        else
            purchaseQuery = dBase.collection("purchases").whereEqualTo("cookUID", uid)
                    .whereEqualTo("status", "PENDING");

        purchaseQuery.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Log.d(TAG, document.getId() + "=>" + document.getData());
                        Purchase p = document.toObject(Purchase.class);
                        purchaseList.add(p);
                    }
                    purchases.setValue(purchaseList);
                });
    }

    public LiveData<User> getUser(String uid) {
        if (uid.isEmpty()) {
            currentUser = new MutableLiveData<>();
            loadUser();
            return currentUser;
        }
        cookUser = new MutableLiveData<>();
        loadUser(uid);
        return cookUser;

    }

    public void loadUser() {
        Mealer app = (Mealer) getApplication().getApplicationContext();
        app.initializeUser(result -> {
            Log.d(TAG, result.getFirstName());
            currentUser.setValue(result);
        });
    }

    public void loadUser(String uid) {
        Mealer app = (Mealer) getApplication().getApplicationContext();
        app.getUser(uid, result -> {
            Log.d(TAG, result.getFirstName());
            cookUser.setValue(result);
        });
    }


}
