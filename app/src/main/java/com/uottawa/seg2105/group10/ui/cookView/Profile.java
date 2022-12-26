package com.uottawa.seg2105.group10.ui.cookView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawa.seg2105.group10.Mealer;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Cook;
import com.uottawa.seg2105.group10.repositories.Purchase;
import com.uottawa.seg2105.group10.repositories.User;
import com.uottawa.seg2105.group10.ui.clientView.PurchasesViewModel;
import com.uottawa.seg2105.group10.ui.recyclers.Purchase_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Objects;

public class Profile extends AppCompatActivity implements RecyclerViewInterface {

    private PurchasesViewModel model;
    public ArrayList<Purchase> purchases;
    private static final String TAG = "Profile";
    RecyclerView recyclerView;

    private User cookUser, currentUser;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        model = new ViewModelProvider(this).get(PurchasesViewModel.class);

        type = getIntent().getStringExtra("type");

        TextView purchaseTextView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.purchaseRecyclerView);
        TextView cookName = findViewById(R.id.cookNameTextView);
        TextView cookDescription = findViewById(R.id.descProfTextView);
        TextView cookAddress = findViewById(R.id.addressTextView);
        TextView cookEmail = findViewById(R.id.emailAddressTextView);
        TextView cookCompletedOrders = findViewById(R.id.numOfMealsSoldTextView);
        TextView cookRating = findViewById(R.id.ratingTextView);

        MutableLiveData<User> tempUser = new MutableLiveData<>();
        Mealer app = (Mealer) getApplication().getApplicationContext();
        app.initializeUser(result -> {
            Log.d("TAG", result.getFirstName());
            currentUser = result;
            if (Objects.equals(type, "Client")) {
                purchaseTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                app.getUser(getIntent().getStringExtra("cookUID"), cookResult -> {
                    Log.d("TAG", cookResult.getFirstName());
                    cookUser = cookResult;
                });
            } else cookUser = currentUser;

            String name = cookUser.getFirstName() + " " + cookUser.getLastName();
            cookName.setText(name);
            cookDescription.setText(((Cook)cookUser).getDescription());
            cookAddress.setText(((Cook)cookUser).getAddress());
            cookEmail.setText(((Cook)cookUser).getEmail());
            String orders = ((Cook)cookUser).getCompletedOrders() + "";
            cookCompletedOrders.setText(orders);
            String rating = ((Cook)cookUser).getRating() + "";
            cookRating.setText(rating);
        });
        purchases = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (type.equals("Cook")) {
            purchases.clear();
            setUpPurchaseModels();
        }
    }

    private void updateView() {
        Purchase_RecyclerViewAdapter adapter = new Purchase_RecyclerViewAdapter("Cook", this, purchases, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setUpPurchaseModels() {
        model.getPurchases().observe(Profile.this, list -> {
            purchases.addAll(list);
            updateView();
        });
    }

    // Should not be clickable
    @Override
    public void onItemClick(int position) {
    }
}