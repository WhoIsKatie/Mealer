package com.uottawa.seg2105.group10.ui.cookView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Purchase;
import com.uottawa.seg2105.group10.ui.recyclers.Purchase_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class Profile extends AppCompatActivity implements RecyclerViewInterface {

    public ArrayList<Purchase> purchases;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private DocumentReference userRef;
    private DocumentSnapshot document;
    private TextView purchaseTextView;
    private RecyclerView purchaseRecyclerView;
    private static final String TAG = "Profile";
    private String currentCookUID;
    RecyclerView recyclerView;
    private final DocumentSnapshot[] userSnapshot = new DocumentSnapshot[1];
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        String userUID = mAuth.getCurrentUser().getUid();
        userRef = dBase.collection("users").document(userUID);
        purchaseTextView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.purchaseRecyclerView);

        userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userSnapshot[0] = task.getResult();
                        if (userSnapshot[0].exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + userSnapshot[0].getData());
                            currentCookUID = userSnapshot[0].getString("uid");
                        }
                    }
                });

        /*userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    type[0] = document.getString("type");
                }

            }

        });*/

        type = getIntent().getStringExtra("type");

        TextView cookName = findViewById(R.id.cookNameTextView);
        String name = getIntent().getStringExtra("firstName") + " " + getIntent().getStringExtra("lastName");
        cookName.setText(name);

        TextView cookDescription = findViewById(R.id.descProfTextView);
        String description = getIntent().getStringExtra("description");
        cookDescription.setText(description);

        TextView cookAddress = findViewById(R.id.addressTextView);
        String address = getIntent().getStringExtra("address");
        cookAddress.setText(address);

        TextView cookEmail = findViewById(R.id.emailAddressTextView);
        String email = getIntent().getStringExtra("email");
        cookEmail.setText(email);

        TextView cookCompletedOrders = findViewById(R.id.numOfMealsSoldTextView);
        String completedOrders = getIntent().getStringExtra("completedOrders");
        cookCompletedOrders.setText(completedOrders);

        TextView cookRating = findViewById(R.id.ratingTextView);
        DecimalFormat decfor = new DecimalFormat("0.00");
        Double ratingSum = getIntent().getDoubleExtra("ratingSum", 0);
        Double numReviews = getIntent().getDoubleExtra("numReviews", 0);
        String rating = decfor.format(ratingSum / numReviews);
        if (rating == null) {
            cookRating.setText("Undetermined");
        } else {
            cookRating.setText(rating);
        }

        purchases = new ArrayList<Purchase>();
        recyclerView = findViewById(R.id.purchaseRecyclerView);
        if (type.equals("Client")) {
            purchaseTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (type.equals("Cook")) {
            purchases.clear();
            setUpPurchase();
        }
    }

    private void updateView() {
        Purchase_RecyclerViewAdapter adapter = new Purchase_RecyclerViewAdapter("Cook", this, purchases, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setUpPurchase() {
        // initializing all lists of fields for complaints that are active
        ArrayList<String> status = new ArrayList<>();
        ArrayList<String> clientName = new ArrayList<>();
        ArrayList<String> cookName = new ArrayList<>();
        ArrayList<String> pickUpTime = new ArrayList<>();
        ArrayList<String> cookUID = new ArrayList<>();
        ArrayList<String> clientUID = new ArrayList<>();
        ArrayList<String> mealID = new ArrayList<>();
        ArrayList<String> imageID = new ArrayList<>();
        ArrayList<String> documents = new ArrayList<>();

        // used official docs: https://firebase.google.com/docs/firestore/query-data/queries#simple_queries
        dBase.collection("purchases").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                Log.d(TAG, document.getId() + "=>" + document.getData());
                Map<String, Object> data = document.getData();
                status.add(data.get("status").toString());
                clientName.add(data.get("clientName").toString());
                cookName.add(data.get("cookName").toString());
                pickUpTime.add(data.get("pickUpTime").toString());
                cookUID.add(data.get("cookUID").toString());
                mealID.add(data.get("mealID").toString());
                imageID.add(data.get("imageID").toString());
                clientUID.add(data.get("clientUID").toString());
                documents.add(document.getReference().getId());
            }
            for (int i = 0; i < mealID.size(); i++) {
                Purchase cm = new Purchase(documents.get(i), cookUID.get(i), clientUID.get(i), mealID.get(i), imageID.get(i), pickUpTime.get(i), clientName.get(i), cookName.get(i), status.get(i));
                if (status.get(i).equalsIgnoreCase("REJECTED") || !(cookUID.get(i).equals(currentCookUID))) {
                    continue;
                }
                purchases.add(cm);
            }
            updateView();
        });
    }

    // Should not be clickable
    @Override
    public void onItemClick(int position) {
    }
}