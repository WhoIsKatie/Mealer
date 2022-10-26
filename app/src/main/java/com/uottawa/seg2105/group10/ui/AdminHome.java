package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.temp.ComplaintModel;
import com.uottawa.seg2105.group10.temp.Complaint_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.temp.RecyclerViewInterface;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<ComplaintModel> complaintModel = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private DocumentSnapshot document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);

        RecyclerView recyclerView = findViewById(R.id.complaint_recycler_view);

        setUpComplaintModels();

        Complaint_RecyclerViewAdapter adapter = new Complaint_RecyclerViewAdapter(this, complaintModel, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        dBase = FirebaseFirestore.getInstance();
    }

    private void setUpComplaintModels(){
        //TODO: add new field to complaints for status: active, tempSuspend, indefSuspend, dismssed
        Query query = dBase.collection("complaints");
        //FirestoreRecyclerOptions


            //TODO: delete strings => iterate over database complaints collection
        /*String[] namesOfUsers = getResources().getStringArray(R.array.names_of_unsatisfied_customers);
        String[] timesOfComplaint = getResources().getStringArray(R.array.times_of_complaint);
        String[] titleOfComplaint = getResources().getStringArray(R.array.title_of_complaint);
        String[] descriptionOfComplaint = getResources().getStringArray(R.array.description_of_complaint);
        String[] cookOfComplaint = getResources().getStringArray(R.array.cook_concerned_by_complaint);

        for (int i = 0; i < namesOfUsers.length; i++){
            complaintModel.add(new ComplaintModel(namesOfUsers[i], timesOfComplaint[i], titleOfComplaint[i], descriptionOfComplaint[i], cookOfComplaint[i]));
        }*/
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AdminHome.this, ComplaintView.class);

        intent.putExtra("NAME", complaintModel.get(position).getNameOfUser());
        intent.putExtra("TIME", complaintModel.get(position).getTimeOfComplaint());
        intent.putExtra("TITLE", complaintModel.get(position).getTitleOfComplaint());
        intent.putExtra("DESCRIPTION", complaintModel.get(position).getDescriptionOfComplaint());
        intent.putExtra("COOK", complaintModel.get(position).getCookComplaint());

        startActivity(intent);
    }
}
