package com.uottawa.seg2105.group10.recyclers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.ui.MealView;

import java.util.ArrayList;

public class Meal_RecyclerViewAdapter extends RecyclerView.Adapter<Meal_RecyclerViewAdapter.MyViewHolder>{

private final RecyclerViewInterface recyclerViewInterface;
        Context context;
        ArrayList<Meal> meals;
    Intent intent = ((Activity) context).getIntent();
        String image =intent.getStringExtra("IMAGE");

public Meal_RecyclerViewAdapter(Context context, ArrayList<Meal> meals, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.meals = meals;
        this.recyclerViewInterface = recyclerViewInterface;
        }

@NonNull
@Override
public Meal_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This method inflates the layout and gives a look to our rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.compressed_menu_card_view_format, parent, false);
        return new Meal_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
        }

    @Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // This method assigns values to our rows as they come back on the screen, given the position of the recycler view
        holder.name.setText(meals.get(position).getMealName());
        holder.price.setText(Float.toString(meals.get(position).getPrice()));
        if(meals.get(position).getImageID() != null){
            //holder.mealImage.setImageResource(Integer.parseInt(meals.get(position).getImageID()));
            //todo: JAKE THIS METHOD ONLY WORKS FOR IMAES IN RESOURCES, NEED USE A DIFF METHOD
        }
        }

@Override
public int getItemCount() {
        // Returns the total number of items in the RecyclerView
        return meals.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView mealImage;
    TextView name, price, view;


    public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);

        name = itemView.findViewById(R.id.compMealName);
        price = itemView.findViewById(R.id.compMealPrice);
        mealImage = itemView.findViewById(R.id.compMealImgView);
        view = itemView.findViewById(R.id.textView6);

        // Downloading meal image from Firebase Storage and setting mealImageView to the uri with Glide.
        if (image != null) {
            StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(image);
            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(itemView).load(uri).into(mealImage);
            });
        }

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
}
}}