package com.uottawa.seg2105.group10.ui.adminView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Complaint;
import com.uottawa.seg2105.group10.ui.recyclers.Complaint_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;
import com.uottawa.seg2105.group10.ui.ComplaintView;

import java.util.ArrayList;
import java.util.Map;

public class AdminHome extends AppCompatActivity implements RecyclerViewInterface {

    public ArrayList<Complaint> complaints;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "AdminHome";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        complaints = new ArrayList<>();
        recyclerView = findViewById(R.id.complaint_recycler_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        complaints.clear();
        setUpComplaintModels();
    }

    private void updateView() {
        Complaint_RecyclerViewAdapter adapter = new Complaint_RecyclerViewAdapter(this, complaints, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setUpComplaintModels() {
        // initializing all lists of fields for complaints that are active
        ArrayList<String> clientName = new ArrayList<>();
        ArrayList<String> descriptionOfComplaint = new ArrayList<>();
        ArrayList<String> titleOfComplaint = new ArrayList<>();
        ArrayList<String> timeOfComplaint = new ArrayList<>();
        ArrayList<String> cookName = new ArrayList<>();
        ArrayList<String> cookUid = new ArrayList<>();
        ArrayList<String> clientUid = new ArrayList<>();
        ArrayList<String> documents = new ArrayList<>();

        // used official docs: https://firebase.google.com/docs/firestore/query-data/queries#simple_queries
        dBase.collection("complaints").whereEqualTo("status", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Log.d(TAG, document.getId() + "=>" + document.getData());
                    Map<String, Object> data = document.getData();
                    clientName.add(data.get("clientName").toString());
                    cookName.add(data.get("cookName").toString());
                    titleOfComplaint.add(data.get("title").toString());
                    descriptionOfComplaint.add(data.get("description").toString());
                    timeOfComplaint.add(data.get("time").toString());
                    cookUid.add(data.get("cookUid").toString());
                    clientUid.add(data.get("clientUid").toString());
                    documents.add(document.getReference().getId());
                }
                for (int i = 0; i < titleOfComplaint.size(); i++) {
                    Complaint cm = new Complaint(clientName.get(i), cookName.get(i), timeOfComplaint.get(i), titleOfComplaint.get(i), descriptionOfComplaint.get(i), cookUid.get(i), clientUid.get(i));
                    cm.setDocID(documents.get(i));
                    complaints.add(cm);
                }
                updateView();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AdminHome.this, ComplaintView.class);
        Complaint doc = complaints.get(position);
        intent.putExtra("CLIENT NAME", doc.getClientName());
        intent.putExtra("DOCUMENT", doc.getDocID());
        intent.putExtra("COOK NAME", doc.getCookName());
        intent.putExtra("TIME", doc.getTime());
        intent.putExtra("TITLE", doc.getTitle());
        intent.putExtra("DESCRIPTION", doc.getDescription());
        intent.putExtra("COOK", doc.getCookUid());
        intent.putExtra("CLIENT", doc.getClientUid());
        startActivity(intent);
    }
}
