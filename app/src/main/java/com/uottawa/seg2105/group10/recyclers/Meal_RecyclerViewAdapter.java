package com.uottawa.seg2105.group10.recyclers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.ui.AddMeal;
import com.uottawa.seg2105.group10.ui.Menu;

import java.util.ArrayList;

public class Meal_RecyclerViewAdapter extends RecyclerView.Adapter<Meal_RecyclerViewAdapter.MyViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Meal> meals;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    DocumentReference firebaseMeal, userRef;

    public Meal_RecyclerViewAdapter(Context context, ArrayList<Meal> meals, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.meals = meals;
        this.recyclerViewInterface = recyclerViewInterface;
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
    }

    @NonNull
    @Override
    public Meal_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This method inflates the layout and gives a look to our rows
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.compressed_menu_card_view_format, parent, false);
        return new Meal_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // This method assigns values to our rows as they come back on the screen, given the position of the recycler view
        holder.name.setText(meals.get(holder.getLayoutPosition()).getMealName());
        String price = meals.get(holder.getLayoutPosition()).getPrice() + "";
        holder.price.setText(price);

        userRef.collection("meals").limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().isEmpty()){ // no meals sub-collection
                    String cookUID = meals.get(holder.getAbsoluteAdapterPosition()).getDocID();
                    firebaseMeal = dBase.collection("users").document(cookUID).collection("meals").document(meals.get(holder.getAbsoluteAdapterPosition()).getMealName());
                }
                else{
                    firebaseMeal = userRef.collection("meals").document(meals.get(holder.getAdapterPosition()).getMealName());
                }
            }
        });

        firebaseMeal.get().addOnSuccessListener(snapshot -> {
            if(Boolean.TRUE.equals(snapshot.getBoolean("offered"))) {
                holder.offerStatus.setText("Offered");
                holder.offerStatus.setTextColor(context.getResources().getColor(R.color.forest_moss));
                holder.menuModifyButt.setTextColor(context.getResources().getColor(R.color.forest_moss));
                holder.backgroundCard.setCardBackgroundColor(context.getResources().getColor(R.color.flour));
            }
            else {
                holder.offerStatus.setText("Not Offered");
                holder.offerStatus.setTextColor(context.getResources().getColor(R.color.main_yellow));
                holder.menuModifyButt.setTextColor(context.getResources().getColor(R.color.froggy_leaf_green));
                holder.backgroundCard.setCardBackgroundColor(context.getResources().getColor(R.color.black_overlay));
            }
            if(meals.get(holder.getAdapterPosition()).getImageID() != null) {
                StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(meals.get(holder.getAdapterPosition()).getImageID());
                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(context).load(uri).into(holder.mealImage);
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        // Returns the total number of items in the RecyclerView
        return meals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImage;
        TextView name, price, view, offerStatus;
        //Switch menuOfferToggle;
        Button menuRemoveButt, menuModifyButt;
        CardView backgroundCard;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.compMealName);
            price = itemView.findViewById(R.id.compMealPrice);
            mealImage = itemView.findViewById(R.id.compMealImgView);
            view = itemView.findViewById(R.id.textView6);
            offerStatus = itemView.findViewById(R.id.offerStatusTextView);
            //menuOfferToggle = itemView.findViewById(R.id.menuOfferToggle);
            offerStatus = itemView.findViewById(R.id.offerStatusTextView);
            menuRemoveButt = itemView.findViewById(R.id.menuRemoveButt);
            menuModifyButt = itemView.findViewById(R.id.menuModifyButt);
            backgroundCard = itemView.findViewById(R.id.backgroundCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            menuModifyButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddMeal.class);
                    firebaseMeal = userRef.collection("meals").document(meals.get(getAdapterPosition()).getMealName());
                    firebaseMeal.get().addOnSuccessListener(snapshot -> {
                        Meal thisMeal = snapshot.toObject(Meal.class);
                        intent.putExtra("MEAL NAME", thisMeal.getMealName());
                        intent.putExtra("PRICE", thisMeal.getPrice());
                        intent.putExtra("DESCRIPTION", thisMeal.getDescription());
                        intent.putExtra("IMAGE", thisMeal.getImageID());
                        context.startActivity(intent);
                    });
                }
            });

            menuRemoveButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (offerStatus.getText() == "Offered"){
                        Toast.makeText(context, "You cannot remove this meal as it is currently being offered.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
                        userRef.collection("meals").document(meals.get(getAdapterPosition()).getMealName()).delete();
                        Toast.makeText(context, "The meal has been successfully removed.",
                                Toast.LENGTH_SHORT).show();

                    }
                    Intent intent = new Intent(context, Menu.class);
                    context.startActivity(intent);
                }
            });
        }
    }}