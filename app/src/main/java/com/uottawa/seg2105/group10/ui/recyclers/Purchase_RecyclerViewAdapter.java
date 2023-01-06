package com.uottawa.seg2105.group10.ui.recyclers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Complaint;
import com.uottawa.seg2105.group10.repositories.Purchase;
import com.uottawa.seg2105.group10.ui.cookView.Profile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

public class Purchase_RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "Purchase_Adapter";
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Purchase> purchases;
    private DocumentSnapshot document;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    DocumentReference userRef, firebasePurchase;
    String cookUID, cookName;
    private String type;
    private int pos;

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
            clientViewHolder.position = clientViewHolder.getBindingAdapterPosition();
            clientViewHolder.purchase = purchases.get(clientViewHolder.getBindingAdapterPosition());

            cookUID = clientViewHolder.purchase.getCookUID();
            cookName = clientViewHolder.purchase.getCookName();
            String purchasedMealName = clientViewHolder.purchase.getMealID();
            String pickUpTime = clientViewHolder.purchase.getPickupTime();
            String status = clientViewHolder.purchase.getStatus().toUpperCase();
            long requestedMillis = Long.parseLong(clientViewHolder.purchase.getRequestTime());
            LocalDateTime requestedStamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(requestedMillis), ZoneId.systemDefault());

            clientViewHolder.mealName.setText(purchasedMealName);
            clientViewHolder.cookNameText.setText(cookName);
            clientViewHolder.pickupTime.setText(pickUpTime);
            clientViewHolder.purchaseStatus.setText(status);
            clientViewHolder.requestTime.setText(requestedStamp.truncatedTo(ChronoUnit.MINUTES).toString());

            switch (status) {
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
                case "ACCEPTED":
                    clientViewHolder.purchaseStatus.setTextColor(context.getResources().getColor(R.color.froggy_leaf_green));
                    break;
            }

            if (clientViewHolder.purchase.isRated())
                clientViewHolder.rateCook.setVisibility(View.GONE);
            if (clientViewHolder.purchase.isComplained())
                clientViewHolder.complain.setVisibility(View.GONE);

            if (clientViewHolder.purchase.getImageID() != null) {
                StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(clientViewHolder.purchase.getImageID());
                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(context).load(uri).into(clientViewHolder.mealImg);
                });
            }

        } else if (type.equals("Cook")) {
            CookViewHolder cookViewHolder = (CookViewHolder) holder;
            cookViewHolder.position = cookViewHolder.getBindingAdapterPosition();
            cookViewHolder.purchase = purchases.get(cookViewHolder.position);

            String purchaseStatus = cookViewHolder.purchase.getStatus().toUpperCase();
            cookViewHolder.mealName.setText(cookViewHolder.purchase.getMealID());
            cookViewHolder.clientName.setText(cookViewHolder.purchase.getClientName());
            cookViewHolder.pickUpTime.setText(cookViewHolder.purchase.getPickupTime());

            if (purchaseStatus.equalsIgnoreCase("accepted")) {
                cookViewHolder.approveButt.setVisibility(View.GONE);
                cookViewHolder.rejectButt.setVisibility(View.GONE);
                cookViewHolder.approvedMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        // Returns the total number of items in the RecyclerView
        return purchases.size();
    }

    public class CookViewHolder extends RecyclerView.ViewHolder {

        TextView mealName, clientName, pickUpTime, approvedMessage;
        Button approveButt, rejectButt;
        Purchase purchase;
        int position;

        public CookViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            mealName = itemView.findViewById(R.id.compMealNameTextView);
            clientName = itemView.findViewById(R.id.compClientNameTextView);
            pickUpTime = itemView.findViewById(R.id.compPickUpTimeTextView);
            approveButt = itemView.findViewById(R.id.compApproveButt);
            rejectButt = itemView.findViewById(R.id.compRejectButt);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int pos = getBindingAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });

            approveButt.setOnClickListener(v -> {
                purchase.updateStatus(Purchase.ACCEPTED.toUpperCase(Locale.ROOT));

                userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            userRef.update("completedOrders", FieldValue.increment(1));
                        }
                    }
                });

                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("type", "Cook");
                context.startActivity(intent);
                ((Activity)context).finish();
            });

            rejectButt.setOnClickListener(v -> {
                purchase.updateStatus(Purchase.REJECTED.toUpperCase(Locale.ROOT));

                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("type", "Cook");
                context.startActivity(intent);
                ((Activity)context).finish();
            });
        }
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder {

        EditText complaint, title;
        TextView requestTime, mealName, cookNameText, price, clientHeader, pickupTime, purchaseStatus;
        Button complain, rateCook;
        ImageView mealImg;

        Purchase purchase;
        int position;

        public ClientViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            requestTime = itemView.findViewById(R.id.requestTime);
            mealName = itemView.findViewById(R.id.purchasedName);
            cookNameText = itemView.findViewById(R.id.purchasedCook);
            price = itemView.findViewById(R.id.purchasedPrice);
            pickupTime = itemView.findViewById(R.id.clientPickupTime);
            purchaseStatus = itemView.findViewById(R.id.purchaseStatus);
            mealImg = itemView.findViewById(R.id.purchaseMealImage);
            clientHeader = itemView.findViewById(R.id.clientNameHeadline);

            complain = itemView.findViewById(R.id.complain);
            rateCook = itemView.findViewById(R.id.rateCook);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                        recyclerViewInterface.onItemClick(pos);
                }
            });

            complain.setOnClickListener(view -> {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                final View complaintPopup = LayoutInflater.from(context).inflate(R.layout.complaint_dialog, null);

                title = complaintPopup.findViewById(R.id.titleComplaint);
                complaint = complaintPopup.findViewById(R.id.complaint);

                Button submitButton = complaintPopup.findViewById(R.id.submitButton);
                Button cancelButton = complaintPopup.findViewById(R.id.cancelButton);


                dialogBuilder.setView(complaintPopup);
                AlertDialog dialog = dialogBuilder.create();
                dialog.getWindow().setBackgroundDrawableResource(R.color.flour_overlay);
                dialog.show();

                cancelButton.setOnClickListener(view1 -> dialog.dismiss());

                submitButton.setOnClickListener(view2 -> {
                    String complaintTitle = title.getText().toString();
                    String complaintDesc = complaint.getText().toString();
                    if (!validateTitle() | !validateComplaint()) {
                        return;
                    }
                    Complaint c = new Complaint(mAuth.getCurrentUser().getDisplayName(), cookName,
                            String.valueOf(LocalTime.now()), complaintTitle, complaintDesc,
                            cookUID, mAuth.getCurrentUser().getUid());

                    dBase.collection("complaints").add(c)
                            .addOnSuccessListener(documentReference -> {
                                Log.d("ClientHome", "DocumentSnapshot written with ID: " + documentReference.getId());
                                Toast.makeText(context, "Complaint submitted", Toast.LENGTH_SHORT).show();
                                c.setDocID(documentReference.getId());
                                documentReference.update("docID", c.getDocID());
                            });
                    purchase.setComplained();
                    dialog.dismiss();

                    if (position != RecyclerView.NO_POSITION) {
                        if (getBindingAdapter() != null)
                            getBindingAdapter().notifyItemChanged(position);
                    }
                });
            });

            rateCook.setOnClickListener(view -> {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                final View ratePopup = LayoutInflater.from(context).inflate(R.layout.ratecook_dialog, null);

                RatingBar ratingBar = ratePopup.findViewById(R.id.ratingBar);
                Button submitButt = ratePopup.findViewById(R.id.ratingSubmitButt);
                Button cancelButt = ratePopup.findViewById(R.id.ratingCancelButt);

                dialogBuilder.setView(ratePopup);
                AlertDialog dialog = dialogBuilder.create();
                dialog.getWindow().setBackgroundDrawableResource(R.color.flour_overlay);
                dialog.show();

                cancelButt.setOnClickListener(view3 -> dialog.dismiss());

                submitButt.setOnClickListener(view2 -> {
                    DocumentReference cookRef = dBase.collection("users").document(cookUID);
                    cookRef.update("ratingSum", FieldValue.increment(ratingBar.getRating()))
                            .addOnSuccessListener(v -> {
                                Log.d("ClientHome", "DocumentSnapshot successfully updated!");
                                cookRef.update("numReviews", FieldValue.increment(1));
                                Toast.makeText(context, "Rating submitted", Toast.LENGTH_SHORT).show();
                            });
                    purchase.setRated();
                    dialog.dismiss();

                    if (position != RecyclerView.NO_POSITION) {
                        if (getBindingAdapter() != null)
                            getBindingAdapter().notifyItemChanged(position);
                    }
                });

            });
        }

        private boolean validateComplaint() {
            String val = complaint.getText().toString().trim();

            if (val.isEmpty()) {
                complaint.setError("Field cannot be empty");
                return false;
            }

            complaint.setError(null);
            return true;
        }

        private boolean validateTitle() {
            String val = title.getText().toString().trim();

            if (val.isEmpty()) {
                title.setError("Field cannot be empty");
                return false;
            }

            title.setError(null);
            return true;
        }
    }
}



