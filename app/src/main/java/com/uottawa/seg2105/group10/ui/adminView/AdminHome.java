package com.uottawa.seg2105.group10.ui.adminView;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Complaint;
import com.uottawa.seg2105.group10.ui.ComplaintView;
import com.uottawa.seg2105.group10.ui.recyclers.Complaint_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity implements RecyclerViewInterface {

    private AdminHomeViewModel model;
    public ArrayList<Complaint> complaints;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "AdminHome";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);
        model = new ViewModelProvider(this).get(AdminHomeViewModel.class);

        complaints = new ArrayList<>();
        recyclerView = findViewById(R.id.complaint_recycler_view);

        //TODO: implement logout button
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
        model.getComplaints().observe(AdminHome.this, list -> {
            complaints.addAll(list);
            updateView();
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AdminHome.this, ComplaintView.class);
        Complaint c = complaints.get(position);
        intent.putExtra("CLIENT NAME", c.getClientName());
        intent.putExtra("DOCUMENT", c.getDocID());
        intent.putExtra("COOK NAME", c.getCookName());
        intent.putExtra("TIME", c.getTime());
        intent.putExtra("TITLE", c.getTitle());
        intent.putExtra("DESCRIPTION", c.getDescription());
        intent.putExtra("COOK", c.getCookUid());
        intent.putExtra("CLIENT", c.getClientUid());
        startActivity(intent);
    }
}
