package com.uottawa.seg2105.group10.ui.cookView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Meal;
import com.uottawa.seg2105.group10.ui.AddMeal;
import com.uottawa.seg2105.group10.ui.MealView;
import com.uottawa.seg2105.group10.ui.recyclers.Meal_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;


public class CookHome extends AppCompatActivity implements RecyclerViewInterface {

    private CookHomeViewModel model;
    public ArrayList<Meal> meals;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private DocumentReference userRef;
    private static final String TAG = "Menu";
    RecyclerView recyclerView;
    private Button addMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookhome);
        model = new ViewModelProvider(this).get(CookHomeViewModel.class);

        meals = new ArrayList<>();
        recyclerView = findViewById(R.id.mealsRecyclerView);

        addMeal = findViewById(R.id.addMeal);
        addMeal.setOnClickListener(view -> {
            startActivity(new Intent(CookHome.this, AddMeal.class));
        });

        //TODO: implement logout button
    }

    @Override
    protected void onStart() {
        super.onStart();
        meals.clear();
        setUpMealModels();
    }

    private void updateView() {
        Meal_RecyclerViewAdapter adapter = new Meal_RecyclerViewAdapter("Cook", this, meals, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setUpMealModels() {
        model.getMeals().observe(CookHome.this, list -> {
            meals.addAll(list);
            updateView();
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(CookHome.this, MealView.class);
        Meal m = meals.get(position);
        intent.putExtra("MEAL NAME", m.getMealName());
        intent.putExtra("PRICE", m.getPrice());
        intent.putExtra("MEAL TYPE", m.getMealType());
        intent.putExtra("CUISINE", m.getCuisine());
        intent.putExtra("DESCRIPTION", m.getDescription());
        intent.putExtra("INGREDIENTS", m.getIngredients());
        intent.putExtra("ALLERGENS", m.getAllergens());
        intent.putExtra("IMAGE", m.getImageID());
        intent.putExtra("OFFERED", m.getOfferStatus());
        intent.putExtra("COOKUID", m.getCookUID());
        startActivity(intent);
    }
}
