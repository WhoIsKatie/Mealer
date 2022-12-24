package com.uottawa.seg2105.group10.ui.clientView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Purchase;
import com.uottawa.seg2105.group10.ui.recyclers.Purchase_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Objects;


public class ClientHome extends AppCompatActivity implements RecyclerViewInterface {

    private ClientHomeViewModel model;
    //initializing variables or instances
    protected RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private static final String TAG = "Client Home";
    private ImageButton searchButton;
    private CollectionReference purchaseRef;
    private ArrayList<Purchase> purchases;
    String clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienthome);
        model = new ViewModelProvider(this).get(ClientHomeViewModel.class);

        // Initialize Firebase Authority and Firebase Firestore objects
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.clientRecyclerView);
        searchButton = findViewById(R.id.searchButton);
        TextView clientNameHeadline = (TextView) findViewById(R.id.clientNameHeadline);

        clientName = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
        clientNameHeadline.setText(clientName);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClientHome.this, MealSearch.class));
            }
        });

        //TODO: implement logout button
    }

    @Override
    protected void onStart() {
        super.onStart();
        purchases = new ArrayList<>();
        setUpPurchaseModels();
    }

    private void setUpPurchaseModels() {
        model.getPurchases().observe(ClientHome.this, list -> {
            purchases.addAll(list);
            updateView();
        });
    }

    private void updateView() {
        Purchase_RecyclerViewAdapter purchasesRVAdapter = new Purchase_RecyclerViewAdapter("Client", this, purchases, this);
        recyclerView.setAdapter(purchasesRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }


    // Should not be clickable
    @Override
    public void onItemClick(int position) {}
}
