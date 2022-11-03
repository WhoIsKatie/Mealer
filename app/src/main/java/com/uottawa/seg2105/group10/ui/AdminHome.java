package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.temp.ComplaintModel;
import com.uottawa.seg2105.group10.temp.Complaint_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.temp.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Map;

public class AdminHome extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<ComplaintModel> complaintModel = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "AdminHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.complaint_recycler_view);

        setUpComplaintModels();

        Complaint_RecyclerViewAdapter adapter = new Complaint_RecyclerViewAdapter(this, complaintModel, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }

    private void setUpComplaintModels(){
        // used official docs: https://firebase.google.com/docs/firestore/query-data/queries#simple_queries
        //TODO: add new field to complaints for status: active, tempSuspend, indefSuspend, dismssed

        dBase.collection("complaints").whereEqualTo("status", true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // initializing all lists of fields for complaints that are active
                            ArrayList<String> cookName = new ArrayList<>();
                            ArrayList<String> descriptionOfComplaint = new ArrayList<>();
                            ArrayList<String> titleOfComplaint = new ArrayList<>();
                            ArrayList<Timestamp> timeOfComplaint = new ArrayList<>();
                            ArrayList<String> cookUid = new ArrayList<>();
                            ArrayList<String> clientUid = new ArrayList<>();
                            if(task.getResult() != null){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // fetching values for each field for all active complaints
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Map<String, Object> data = document.getData();
                                    cookName.add(data.get("cookName").toString());
                                    titleOfComplaint.add(data.get("title").toString());
                                    descriptionOfComplaint.add(data.get("description").toString());
                                    timeOfComplaint.add((Timestamp) data.get("time"));
                                    cookUid.add(data.get("cookUid").toString());
                                    clientUid.add(data.get("clientUid").toString());
                                }
                            }


                            for (int i = 0; i < titleOfComplaint.size(); i++){
                                complaintModel.add(new ComplaintModel(cookName.get(i),timeOfComplaint.get(i).toString(), titleOfComplaint.get(i), descriptionOfComplaint.get(i), cookUid.get(i), clientUid.get(i)));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

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

        intent.putExtra("NAME", complaintModel.get(position).getNameOfCook());
        intent.putExtra("TIME", complaintModel.get(position).getTimeOfComplaint());
        intent.putExtra("TITLE", complaintModel.get(position).getTitleOfComplaint());
        intent.putExtra("DESCRIPTION", complaintModel.get(position).getDescriptionOfComplaint());
        intent.putExtra("COOK", complaintModel.get(position).getCookUid());
        intent.putExtra("CLIENT", complaintModel.get(position).getClientUid());

        startActivity(intent);
    }
}
