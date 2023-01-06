package com.uottawa.seg2105.group10.ui.cookView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Cook;
import com.uottawa.seg2105.group10.repositories.Purchase;
import com.uottawa.seg2105.group10.repositories.User;
import com.uottawa.seg2105.group10.ui.recyclers.PurchasesViewModel;
import com.uottawa.seg2105.group10.ui.recyclers.Purchase_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Objects;

public class Profile extends AppCompatActivity implements RecyclerViewInterface {

    private PurchasesViewModel model;
    public ArrayList<Purchase> purchases;
    private static final String TAG = "Profile";
    private RecyclerView recyclerView;
    private TextView cookName, cookDescription, cookAddress, cookCompletedOrders;
    private RatingBar cookRating;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        model = new ViewModelProvider(this).get(PurchasesViewModel.class);

        type = getIntent().getStringExtra("type");

        recyclerView = findViewById(R.id.purchaseRecyclerView);
        cookName = findViewById(R.id.cookNameTextView);
        cookDescription = findViewById(R.id.cookDescription);
        cookAddress = findViewById(R.id.addressTextView);
        cookCompletedOrders = findViewById(R.id.numMealsSold);
        cookRating = findViewById(R.id.cookRating);

        model.getUser("").observe(Profile.this, user -> {
            if (user == null)
                Log.d(TAG, "failed to retrieve user object.");
            else {
                if (Objects.equals(type, "Client")) {
                    CardView purchaseRequestView = findViewById(R.id.purchaseRequestView);
                    purchaseRequestView.setVisibility(View.GONE);
                    String cookUID = getIntent().getStringExtra("cookUID");

                    model.getUser(cookUID).observe(Profile.this, cook -> {
                        if (cook == null)
                            Log.d(TAG, "failed to retrieve user object.");
                        updateTextViews(cook);
                    });
                } else updateTextViews(user);
            }
        });
        purchases = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (type.equals("Cook")) {
            setUpPurchaseModels();
        }
    }

    private void updateTextViews(User user) {
        String name = user.getFirstName() + " " + user.getLastName();
        cookName.setText(name);
        cookDescription.setText(((Cook)user).getDescription());
        cookAddress.setText(((Cook)user).getAddress());
        String orders = ((Cook)user).getCompletedOrders() + " orders";
        cookCompletedOrders.setText(orders);
        cookRating.setRating((float) ((Cook) user).getRating());
    }

    private void updateView() {
        Purchase_RecyclerViewAdapter adapter = new Purchase_RecyclerViewAdapter("Cook", this, purchases, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setUpPurchaseModels() {
        model.getPurchases("Cook").observe(Profile.this, list -> {
            purchases.clear();
            purchases.addAll(list);
            if (purchases.isEmpty()){
                recyclerView.setVisibility(View.GONE);
                TextView recyclerAlert = findViewById(R.id.recyclerAlert);
                recyclerAlert.setVisibility(View.VISIBLE);
            } else
                updateView();
        });
    }

    // Should not be clickable
    @Override
    public void onItemClick(int position) {
    }
}