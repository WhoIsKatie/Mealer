package com.uottawa.seg2105.group10.ui.adminView;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uottawa.seg2105.group10.repositories.Complaint;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeViewModel extends ViewModel {

    private MutableLiveData<List<Complaint>> complaints;

    public LiveData<List<Complaint>> getComplaints() {
        complaints = new MutableLiveData<>();
        loadComplaintsFromFirebase();
        return complaints;
    }

    public void loadComplaintsFromFirebase() {
        FirebaseFirestore dBase = FirebaseFirestore.getInstance();
        ArrayList<Complaint> complaintList = new ArrayList<>();

        // fetch all active complaints
        dBase.collection("complaints").whereEqualTo("status", true)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Log.d("AdminHomeViewModel", document.getId() + "=>" + document.getData());
                    Complaint c = document.toObject(Complaint.class);
                    c.setDocID(document.getId());
                    complaintList.add(c);
                }
                complaints.setValue(complaintList);
            }
        });
    }
}
