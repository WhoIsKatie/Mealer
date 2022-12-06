package com.uottawa.seg2105.group10.recyclers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Purchase;

import java.time.LocalTime;
import java.util.ArrayList;

public class Purchase_RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "Purchase_Adapter";
    private final RecyclerViewInterface recyclerViewInterface;
    private CollectionReference purchaseRef;
    Context context;
    ArrayList<Purchase> purchases;
    private DocumentSnapshot document;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private AlertDialog dialog;
    DocumentReference userRef, clientRef, cookRef, firebasePurchase;
    String clientName, cookUID, cookName;
    private String type;

    public Purchase_RecyclerViewAdapter(String type, Context context, ArrayList<Purchase> purchases, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.purchases = purchases;
        this.recyclerViewInterface = recyclerViewInterface;
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This method inflates the layout and gives a look to our rows
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cook_purchase_card_view, parent, false);

        //if user is client
        if (type.equals("Client")) {
            view = inflater.inflate(R.layout.client_purchase_card_view, parent, false);
            return new ClientViewHolder(view, recyclerViewInterface);
        }
        return new CookViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // This method assigns values to our rows as they come back on the screen, given the position of the recycler view
        if (type.equals("Client")) {
            ClientViewHolder clientViewHolder = (ClientViewHolder) holder;
            cookUID = purchases.get(clientViewHolder.getBindingAdapterPosition()).getCookUID();
                    //cookName = purchases.get(clientViewHolder.getBindingAdapterPosition()).getCookName();
            String purchasedName = purchases.get(clientViewHolder.getBindingAdapterPosition()).getMealID();
            String clientPickupTime = purchases.get(clientViewHolder.getBindingAdapterPosition()).getPickUpTime();
            String purchaseStatus = purchases.get(clientViewHolder.getBindingAdapterPosition()).getStatus().toUpperCase();

            cookRef = dBase.collection("users").document(cookUID);
            cookRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        cookName = document.getString("firstName") + " " + document.getString("lastName");
                    }
                }});

            userRef.get().addOnCompleteListener(task->{
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        clientName = document.getString("firstName") + " " + document.getString("lastName");
                    }
                }
            });

            clientViewHolder.purchasedName.setText(purchasedName);
            clientViewHolder.purchasedCook.setText(cookName);
            clientViewHolder.clientPickupTime.setText(clientPickupTime);
            clientViewHolder.purchaseStatus.setText(purchaseStatus);


            switch (purchaseStatus) {
                case "PENDING":
                    clientViewHolder.complain.setVisibility(View.GONE);
                    clientViewHolder.rateCook.setVisibility(View.GONE);
                    clientViewHolder.purchaseStatus.setTextColor(context.getResources().getColor(R.color.mustard));
                    break;
                case "REJECTED":
                    clientViewHolder.complain.setVisibility(View.GONE);
                    clientViewHolder.rateCook.setVisibility(View.GONE);
                    clientViewHolder.purchaseStatus.setTextColor(context.getResources().getColor(R.color.red));
                    break;
                case " ACCEPTED":
                    clientViewHolder.purchaseStatus.setTextColor(context.getResources().getColor(R.color.froggy_leaf_green));
                    break;
            }

            final String[] imageID = {""};
            cookRef.collection("meals").document(purchasedName).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Object result = task.getResult().get("imageID");
                    if (result != null) {
                        imageID[0] = result.toString();
                        purchases.get(clientViewHolder.getBindingAdapterPosition()).setImageID(purchases.get(clientViewHolder.getBindingAdapterPosition()).getImageID());
                    }
                } else {
                    Log.d(TAG, "Could not fetch mealID");
                }
            });
            if (purchases.get(clientViewHolder.getBindingAdapterPosition()).getImageID() != null) {
                StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(purchases.get(clientViewHolder.getBindingAdapterPosition()).getImageID());
                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(context).load(uri).into(clientViewHolder.mealImage);
                });
            }

        } else if (type.equals("Cook")) {
            CookViewHolder cookViewHolder = (CookViewHolder) holder;
            cookViewHolder.mealName.setText(purchases.get(cookViewHolder.getBindingAdapterPosition()).getMealID());
            cookViewHolder.clientName.setText(purchases.get(cookViewHolder.getBindingAdapterPosition()).getCookName());
            cookViewHolder.pickUpTime.setText(purchases.get(cookViewHolder.getBindingAdapterPosition()).getPickUpTime());
        }
    }

    @Override
    public int getItemCount() {
        // Returns the total number of items in the RecyclerView
        return purchases.size();
    }

    public class CookViewHolder extends RecyclerView.ViewHolder {

        TextView mealName, clientName, pickUpTime;
        Button approveButt, rejectButt;

        public CookViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            mealName = itemView.findViewById(R.id.compMealNameTextView);
            clientName = itemView.findViewById(R.id.compClientNameTextView);
            pickUpTime = itemView.findViewById(R.id.compPickUpTimeTextView);
            approveButt = itemView.findViewById(R.id.compApproveButt);
            rejectButt = itemView.findViewById(R.id.compRejectButt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            approveButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String purchaseStatus = "ACCEPTED";
                    firebasePurchase = dBase.collection("purchases").document(purchases.get(getAdapterPosition()).getRequestTime());
                    firebasePurchase.update("status", purchaseStatus)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                    String completedOrders;
                    userRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                //TODO: Make completedOrders a field for cooks
                                /*completedOrders = String.valueOf(document.get("CompletedOrders"));

                                cookRef.update("completedOrders", completedOrders + 1);*/
                            }
                        }
                    });
                }
            });
            rejectButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String purchaseStatus = "REJECTED";
                    firebasePurchase = dBase.collection("purchases").document(purchases.get(getAdapterPosition()).getRequestTime());
                    firebasePurchase.update("status", purchaseStatus)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }
            });
        }
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder {

        EditText complaint, rate, titleComplaint;
        TextView rateTheCook, explain, requestTime, purchasedName, purchasedCook, purchasedPrice, clientNameHeadline, clientPickupTime, purchaseStatus;

        Button submitButton, cancelButton, complain, rateCook, submitButton2, cancelButton2;
        ImageView mealImage;

        private AlertDialog.Builder dialogBuilder;
        private AlertDialog dialog;

        public ClientViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            requestTime = itemView.findViewById(R.id.requestTime);
            purchasedName = itemView.findViewById(R.id.purchasedName);
            purchasedCook = itemView.findViewById(R.id.purchasedCook);
            purchasedPrice = itemView.findViewById(R.id.purchasedPrice);
            clientPickupTime = itemView.findViewById(R.id.clientPickupTime);
            purchaseStatus = itemView.findViewById(R.id.purchaseStatus);
            mealImage = itemView.findViewById(R.id.purchaseMealImage);
            clientNameHeadline = itemView.findViewById(R.id.clientNameHeadline);


            complain = itemView.findViewById(R.id.complain);
            rateCook = itemView.findViewById(R.id.rateCook);
            purchaseRef = dBase.collection("purchases");
            purchasedCook.setText(cookName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            complain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitComplaint();
                }
            });


            rateCook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rateYourCook();
                }
            });
        }


        public void submitComplaint(){
            dialogBuilder = new AlertDialog.Builder(context);
            final View complaintPopup = LayoutInflater.from(context).inflate(R.layout.activity_complaintpopup, null);

            titleComplaint = complaintPopup.findViewById(R.id.titleComplaint);
            complaint = complaintPopup.findViewById(R.id.complaint);

            submitButton = complaintPopup.findViewById(R.id.submitButton);
            cancelButton = complaintPopup.findViewById(R.id.cancelButton);

            dialogBuilder.setView(complaintPopup);
            dialog = dialogBuilder.create();
            dialog.show();

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String titleComplaintString = titleComplaint.getText().toString();
                    String complaintString = complaint.getText().toString();
                    if(!validateTitle() | !validateComplaint()) {
                        return;
                    }
                    ComplaintModel complaint = new ComplaintModel(clientName, cookName, String.valueOf(LocalTime.now()),titleComplaintString, complaintString, cookUID, mAuth.getCurrentUser().getUid());
                    dBase.collection("complaints").add(complaint)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("ClientHome", "DocumentSnapshot written with ID: " + documentReference.getId());
                                    Toast.makeText(context, "Complaint submitted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("ClientHome", "Error adding document", e);
                                    Toast.makeText(context, "Complaint incomplete", Toast.LENGTH_SHORT).show();
                                }
                            });
                    dialog.dismiss();
                }});

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }

        public void rateYourCook(){
            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(context);
            final View ratePopup = LayoutInflater.from(context).inflate(R.layout.activity_ratecook, null);

            rateTheCook = (TextView) ratePopup.findViewById(R.id.rateTheCook);
            explain = (TextView) ratePopup.findViewById(R.id.explain);

            //TODO: remove cookName.
            rate = (EditText) ratePopup.findViewById(R.id.rate);
            submitButton2 =(Button) ratePopup.findViewById(R.id.submitButton2);
            cancelButton2 = (Button) ratePopup.findViewById(R.id.cancelButton2);

            dialogBuilder2.setView(ratePopup);
            Purchase_RecyclerViewAdapter.this.dialog = dialogBuilder2.create();
            Purchase_RecyclerViewAdapter.this.dialog.show();

            submitButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: validate rating & complaint
                    String rateString = rate.getText().toString();
                    if(!validateRating()) {
                        return;
                    }
                    int rateNum = Integer.parseInt(rateString);
                    clientRef = dBase.collection("users").document(cookUID);
                    clientRef.update("ratingSum", Integer.parseInt(rateString))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("ClientHome", "DocumentSnapshot successfully updated!");
                                     clientRef.update("numReviews", FieldValue.increment(1));
                                    Toast.makeText(context, "Rating submitted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("ClientHome", "Error updating document", e);
                                }
                            });
                    Purchase_RecyclerViewAdapter.this.dialog.dismiss();
                }
            });

            cancelButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Purchase_RecyclerViewAdapter.this.dialog.dismiss();
                }
            });
        }

        private boolean validateComplaint(){
            String val = complaint.getText().toString().trim();

            if(val.isEmpty()) {
                complaint.setError("Field can not be empty");
                return false;
            }
            else{
                complaint.setError(null);
                return true;
            }
        }

        private boolean validateTitle(){
            String val = titleComplaint.getText().toString().trim();

            if(val.isEmpty()) {
                titleComplaint.setError("Field can not be empty");
                return false;
            }
            else{
                titleComplaint.setError(null);
                return true;
            }
        }

        private boolean validateRating(){
            String val = rate.getText().toString();
            String numFormat = "[1-5]";

            if(val.isEmpty()) {
                rate.setError("Field can not be empty");
                return false;
            }

            if (!val.matches(numFormat)){
                rate.setError("Rating must be a number from 1 to 5");
                return false;
            }
            else{
                rate.setError(null);
                return true;
            }
        }

    }
}



