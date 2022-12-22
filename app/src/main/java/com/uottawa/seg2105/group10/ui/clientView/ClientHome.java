package com.uottawa.seg2105.group10.ui.clientView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Purchase;
import com.uottawa.seg2105.group10.ui.recyclers.Purchase_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class ClientHome extends AppCompatActivity implements RecyclerViewInterface {

    //initializing variables or instances
    protected RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private static final String TAG = "Client Home";
    private ImageButton searchButton;
    private CollectionReference purchaseRef;
    private ArrayList<Purchase> purchasesArrayList;
    String clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienthome);

        // Initialize Firebase Authority and Firebase Firestore objects
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore dBase = FirebaseFirestore.getInstance();
        String userName = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = dBase.collection("users").document(userName);
        purchaseRef = dBase.collection("purchases");
        recyclerView = findViewById(R.id.clientRecyclerView);
        searchButton = findViewById(R.id.searchButton);
        TextView clientNameHeadline = (TextView) findViewById(R.id.clientNameHeadline);

        userRef.get().addOnCompleteListener(cookTask -> {
            if (cookTask.isSuccessful()) {
                DocumentSnapshot document = cookTask.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    clientName = document.getString("firstName") + " " + document.getString("lastName");
                }

                clientNameHeadline.setText(clientName);

                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(ClientHome.this, MealSearch.class));
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        purchasesArrayList = new ArrayList<>();
        setUpPurchaseModels();
    }

    private void setUpPurchaseModels() {
        // initializing all lists of fields
        ArrayList<String> cookUID = new ArrayList<>();
        ArrayList<String> clientUID = new ArrayList<>();
        ArrayList<String> mealID = new ArrayList<>();
        ArrayList<String> imageID = new ArrayList<>();
        ArrayList<String> clientName = new ArrayList<>();
        ArrayList<String> cookName = new ArrayList<>();
        ArrayList<String> pickupTime = new ArrayList<>();
        ArrayList<String> requestTime = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();

        purchaseRef.whereEqualTo("clientUID", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + "=>" + document.getData());
                    Map<String, Object> data = document.getData();
                    if (!Objects.equals(data.get("cookUID"), null))
                        cookUID.add((String) data.get("cookUID"));
                    else cookUID.add(null);

                    if (!Objects.equals(data.get("clientUID"), null))
                        clientUID.add((String) data.get("clientUID"));
                    else clientUID.add(null);

                    if (!Objects.equals(data.get("mealID"), null))
                        mealID.add((String) data.get("mealID"));
                    else mealID.add(null);

                    if (!Objects.equals(data.get("clientName"), null))
                        clientName.add((String) data.get("clientName"));
                    else clientName.add(null);

                    if (!Objects.equals(data.get("cookName"), null))
                        cookName.add((String) data.get("cookName"));
                    else cookName.add(null);

                    if (!Objects.equals(data.get("pickUpTime"), null))
                        pickupTime.add((String) data.get("pickUpTime"));
                    else pickupTime.add(null);

                    if (!Objects.equals(data.get("requestTime"), null))
                        requestTime.add((String) data.get("requestTime"));
                    else requestTime.add(null);
                    if (!Objects.equals(data.get("imageID"), null))
                        imageID.add((String) data.get("imageID"));
                    else imageID.add(null);

                    if (!Objects.equals(data.get("status"), null))
                        status.add((String) data.get("status"));
                    else status.add(null);

                    this.clientName = document.getString("clientName");
                }
                for (int i = 0; i < clientUID.size(); i++) {
                    //String imageID, String pickupTime, String cookName, String clientName, PurchaseStatus status
                    Purchase purchase = new Purchase(requestTime.get(i), cookUID.get(i), clientUID.get(i), mealID.get(i), imageID.get(i), pickupTime.get(i), cookName.get(i), clientName.get(i), status.get(i));
                    purchasesArrayList.add(purchase);
                }

                updateClientHome();

            }

        });
    }

    private void updateClientHome() {
        Purchase_RecyclerViewAdapter purchasesRVAdapter = new Purchase_RecyclerViewAdapter("Client", this, purchasesArrayList, this);
        recyclerView.setAdapter(purchasesRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }


    // Should not be clickable
    @Override
    public void onItemClick(int position) {}
}
