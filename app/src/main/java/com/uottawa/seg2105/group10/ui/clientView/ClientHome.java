package com.uottawa.seg2105.group10.ui.clientView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.uottawa.seg2105.group10.Mealer;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Purchase;
import com.uottawa.seg2105.group10.repositories.User;
import com.uottawa.seg2105.group10.ui.Landing;
import com.uottawa.seg2105.group10.ui.recyclers.Purchase_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Objects;


public class ClientHome extends AppCompatActivity implements RecyclerViewInterface {

    private PurchasesViewModel model;
    //initializing variables or instances
    protected RecyclerView recyclerView;
    private ArrayList<Purchase> purchases;
    private User user;
    String clientName;

    @Override
    // Turns off the android back button => User cannot go back to login page unless logged out
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienthome);
        model = new ViewModelProvider(this).get(PurchasesViewModel.class);

        // Initialize Firebase Authority and Firebase Firestore objects
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        purchases = new ArrayList<>();
        recyclerView = findViewById(R.id.clientRecyclerView);
        ImageButton searchButton = findViewById(R.id.searchButt);
        Button logout = findViewById(R.id.clientSignOutButt);
        TextView clientNameHeadline = findViewById(R.id.clientNameHeadline);

        Mealer app = (Mealer) getApplicationContext();

        clientName = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName() + "'s Purchases";
        clientNameHeadline.setText(clientName);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClientHome.this, MealSearch.class));
            }
        });

        logout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(ClientHome.this, Landing.class));
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        purchases.clear();
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
