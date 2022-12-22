package com.uottawa.seg2105.group10.ui.recyclers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Meal;
import com.uottawa.seg2105.group10.ui.AddMeal;
import com.uottawa.seg2105.group10.ui.cookView.Menu;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Meal_RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    private DocumentSnapshot document;
    ArrayList<Meal> meals;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    DocumentReference firebaseMeal, userRef;
    private String type;

    public Meal_RecyclerViewAdapter(String type, Context context, ArrayList<Meal> meals, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.meals = meals;
        this.recyclerViewInterface = recyclerViewInterface;
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        userRef = dBase.collection("users").document(mAuth.getCurrentUser().getUid());
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This method inflates the layout and gives a look to our rows
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.menu_card_view_format, parent, false);
        //if user is client
        if (type.equals("Client")) {
            view = inflater.inflate(R.layout.search_card_view_format, parent, false);
            return new SearchViewHolder(view, recyclerViewInterface);
        }
        return new MenuViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // This method assigns values to our rows as they come back on the screen, given the position of the recycler view
        if(type.equals("Client")) {
            SearchViewHolder searchViewHolder = (SearchViewHolder) holder;
            String cookUID = meals.get(searchViewHolder.getBindingAdapterPosition()).getCookUID();
            userRef = dBase.collection("users").document(cookUID);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        DecimalFormat decfor = new DecimalFormat("0.00");
                        Double ratingSum, numReviews;
                        String rating;
                        String address;
                        if(document.getDouble("ratingSum") != null){
                            ratingSum = document.getDouble("ratingSum");
                            numReviews = document.getDouble("numReviews");
                            rating = decfor.format(ratingSum / numReviews);
                        }
                        else{
                            rating = "Undetermined";
                        }
                        address = document.getString("address");
                        searchViewHolder.location.setText(address);
                        searchViewHolder.rating.setText(rating);
                    }
                }
            });
            searchViewHolder.name.setText(meals.get(searchViewHolder.getBindingAdapterPosition()).getMealName());
            float price = meals.get(searchViewHolder.getBindingAdapterPosition()).getPrice();
            BigDecimal bd = new BigDecimal(price + "");
            String textPrice = bd.setScale(2, RoundingMode.HALF_EVEN).toString();
            searchViewHolder.price.setText(textPrice);
            if(meals.get(searchViewHolder.getBindingAdapterPosition()).getImageID() != null) {
                StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(meals.get(searchViewHolder.getBindingAdapterPosition()).getImageID());
                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(context).load(uri).into(searchViewHolder.mealImage);
                });
            }

        } else if(type.equals("Cook")){
            MenuViewHolder menuViewHolder = (MenuViewHolder) holder;
            menuViewHolder.name.setText(meals.get(menuViewHolder.getBindingAdapterPosition()).getMealName());
            float price = meals.get(menuViewHolder.getBindingAdapterPosition()).getPrice();
            BigDecimal bd = new BigDecimal(price + "");
            String textPrice = bd.setScale(2, RoundingMode.HALF_EVEN).toString();
            menuViewHolder.price.setText(textPrice);

            firebaseMeal = userRef.collection("meals").document(meals.get(menuViewHolder.getBindingAdapterPosition()).getMealName());
            firebaseMeal.get().addOnSuccessListener(snapshot -> {
                if(Boolean.TRUE.equals(snapshot.getBoolean("offered"))) {
                    menuViewHolder.offerStatus.setText("Offered");
                    menuViewHolder.offerStatus.setTextColor(context.getResources().getColor(R.color.forest_moss));
                    menuViewHolder.menuModifyButt.setTextColor(context.getResources().getColor(R.color.forest_moss));
                    menuViewHolder.backgroundCard.setCardBackgroundColor(context.getResources().getColor(R.color.flour));
                }
                else {
                    menuViewHolder.offerStatus.setText("Not Offered");
                    menuViewHolder.offerStatus.setTextColor(context.getResources().getColor(R.color.main_yellow));
                    menuViewHolder.menuModifyButt.setTextColor(context.getResources().getColor(R.color.froggy_leaf_green));
                    menuViewHolder.backgroundCard.setCardBackgroundColor(context.getResources().getColor(R.color.black_overlay));
                    menuViewHolder.name.setTextColor(context.getResources().getColor(R.color.main_yellow));
                }
            });

            if(meals.get(holder.getBindingAdapterPosition()).getImageID() != null) {
                StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(meals.get(menuViewHolder.getBindingAdapterPosition()).getImageID());
                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(context).load(uri).into(menuViewHolder.mealImage);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        // Returns the total number of items in the RecyclerView
        return meals.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImage;
        TextView name, price, view, offerStatus, cookName;
        //Switch menuOfferToggle;
        Button menuRemoveButt, menuModifyButt;
        CardView backgroundCard;

        public MenuViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
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
            cookName = itemView.findViewById(R.id.compressedCookNameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getBindingAdapterPosition();

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
                    firebaseMeal = userRef.collection("meals").document(meals.get(getBindingAdapterPosition()).getMealName());
                    firebaseMeal.get().addOnSuccessListener(snapshot -> {
                        Meal thisMeal = snapshot.toObject(Meal.class);
                        intent.putExtra("MEAL NAME", thisMeal.getMealName());
                        intent.putExtra("PRICE", thisMeal.getPrice());
                        intent.putExtra("DESCRIPTION", thisMeal.getDescription());
                        intent.putExtra("IMAGE", thisMeal.getImageID());
                        intent.putExtra("COOKUID", thisMeal.getCookUID());
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
                        userRef.collection("meals").document(meals.get(getBindingAdapterPosition()).getMealName()).delete();
                        Toast.makeText(context, "The meal has been successfully removed.",
                                Toast.LENGTH_SHORT).show();

                    }
                    Intent intent = new Intent(context, Menu.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImage;
        TextView name, price, rating, location;

        public SearchViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.clientMealTitle);
            price = itemView.findViewById(R.id.clientMealPrice);
            mealImage = itemView.findViewById(R.id.clientMealImgView);
            rating = itemView.findViewById(R.id.rating);
            location = itemView.findViewById(R.id.location);

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
        }
    }
}