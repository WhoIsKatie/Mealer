package com.uottawa.seg2105.group10.ui;

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
import com.uottawa.seg2105.group10.backend.Purchase;
import com.uottawa.seg2105.group10.recyclers.Purchase_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.recyclers.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Map;

public class Profile extends AppCompatActivity implements RecyclerViewInterface {

    public ArrayList<Purchase> purchases;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "Profile";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        purchases = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.purchaseRecyclerView);
        setUpPurchase();
    }

    private void updateView(){
        Purchase_RecyclerViewAdapter adapter = new Purchase_RecyclerViewAdapter(this, purchases, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setUpPurchase(){
        // initializing all lists of fields for complaints that are active
        ArrayList<String> mealName = new ArrayList<>();
        ArrayList<String> clientName = new ArrayList<>();
        ArrayList<String> pickUpTime = new ArrayList<>();
        ArrayList<String> cookUID = new ArrayList<>();
        ArrayList<String> clientUID = new ArrayList<>();
        ArrayList<String> mealID = new ArrayList<>();
        ArrayList<String> documents = new ArrayList<>();

        // used official docs: https://firebase.google.com/docs/firestore/query-data/queries#simple_queries
        dBase.collection("purchases").whereEqualTo("status", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                    Log.d(TAG, document.getId() + "=>" + document.getData());
                    Map<String, Object> data = document.getData();
                    mealName.add(data.get("mealName").toString());
                    clientName.add(data.get("firstName" + "lastName").toString());
                    pickUpTime.add(data.get("pickUpTime").toString());
                    cookUID.add(data.get("cookUID").toString());
                    clientUID.add(data.get("clientUID").toString());
                    documents.add(document.getReference().getId());
                }
                for (int i = 0; i < mealName.size(); i++){
                    Purchase cm = new Purchase(documents.get(i), cookUID.get(i), clientUID.get(i), mealName.get(i), pickUpTime.get(i), clientName.get(i));
                    purchases.add(cm);
                }
                updateView();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}