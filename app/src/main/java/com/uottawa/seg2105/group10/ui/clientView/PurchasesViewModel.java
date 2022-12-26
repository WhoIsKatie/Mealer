package com.uottawa.seg2105.group10.ui.clientView;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.repositories.Purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PurchasesViewModel extends ViewModel {

    private MutableLiveData<List<Purchase>> purchases;

    public LiveData<List<Purchase>> getPurchases() {
        purchases = new MutableLiveData<>();
        loadPurchasesFromFirebase();
        return purchases;
    }

    public void loadPurchasesFromFirebase() {
        FirebaseFirestore dBase = FirebaseFirestore.getInstance();
        ArrayList<Purchase> purchaseList = new ArrayList<>();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // fetch all client's previous purchases
        dBase.collection("purchases").whereEqualTo("clientUID", uid).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Log.d("ClientHomeViewModel", document.getId() + "=>" + document.getData());
                        Purchase p = document.toObject(Purchase.class);
                        purchaseList.add(p);
                    }
                    purchases.setValue(purchaseList);
                });
    }
}
