package com.uottawa.seg2105.group10.ui.clientView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.recyclers.Meal_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.recyclers.RecyclerViewInterface;
import com.uottawa.seg2105.group10.ui.MealView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class MealSearch extends AppCompatActivity implements RecyclerViewInterface {

    public ArrayList<Meal> meals;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "MealSearch";
    RecyclerView recyclerView;
    private EditText mealNameSearch, cuisineTypeSearch, mealTypeSearch;
    protected Meal_RecyclerViewAdapter adapter;
    private Button searchBut;
    public String mealName, cuisineType, mealType;
    public ArrayList<Meal> serchedMeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_search);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        searchBut = findViewById(R.id.searchButt);
        //String userUID = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.mealsRecyclerView);
        mealNameSearch = findViewById(R.id.mealNameSearchBar);
        cuisineTypeSearch = findViewById(R.id.cuisineTypeSearch);
        mealTypeSearch = findViewById(R.id.mealTypeSearchBar);


        meals = new ArrayList<>();
        // ArrayList<String> tess = new ArrayList<String>();
        // tess.add("tess");
        //Meal josh = new Meal(8, "Tess", "Tess","Tess", tess,tess,tess);
        //meals.add(josh);
        adapter = new Meal_RecyclerViewAdapter(this, meals, this);
        updateView();



        mealName = mealNameSearch.getText().toString();
        cuisineType = cuisineTypeSearch.getText().toString();
        mealType = mealTypeSearch.getText().toString();

//changeddd

        searchBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setUpMealModels();
                for (Meal meal : meals) {
                    if (mealName != null) {
                        if (meal.getMealName().equalsIgnoreCase(mealName) && !serchedMeals.contains(meal)) {
                            serchedMeals.add(meal);
                        }
                        if (cuisineType != null) {
                            String[] cuisineList = cuisineType.split(",");
                            for (String cuisine : cuisineList) {
                                for (String mealCuisine : meal.getCuisine()) {
                                    if (cuisine.equalsIgnoreCase(mealCuisine) && !serchedMeals.contains(meal)) {
                                        serchedMeals.add(meal);
                                    }
                                }
                            }
                        }
                        if (mealType != null) {
                            if (meal.getMealType().equalsIgnoreCase(mealType) && !serchedMeals.contains(meal)) {
                                serchedMeals.add(meal);
                            }
                        }
                    }
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpMealModels();
    }

    private void updateView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void setUpMealModels(){
        // initializing all lists of fields
        ArrayList<String> mealName = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> mealType = new ArrayList<>();
        ArrayList<ArrayList<String>> cuisine = new ArrayList<>();
        ArrayList<ArrayList<String>> ingredients = new ArrayList<>();
        ArrayList<ArrayList<String>> allergens = new ArrayList<>();
        ArrayList<Float> price = new ArrayList<>();
        ArrayList<String> image = new ArrayList<>();
        ArrayList<String> documents = new ArrayList<>();
        meals = new ArrayList<>();

        dBase.collection("users").whereEqualTo("type", "Cook").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot cook : task.getResult()) {
                        if(cook.getBoolean("isSuspended")){
                            continue;
                        }
                        cook.getReference().collection("meals").whereEqualTo("offered", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (DocumentSnapshot meal : task.getResult()) {
                                        Log.d(TAG, meal.getId() + "=>" + meal.getData());
                                        Map<String, Object> data = meal.getData();
                                        mealName.add(data.get("mealName").toString());
                                        description.add(data.get("description").toString());
                                        mealType.add(data.get("mealType").toString());
                                        cuisine.add((ArrayList<String>) data.get("cuisine"));
                                        documents.add(meal.getId());
                                        ingredients.add((ArrayList<String>) data.get("ingredients"));
                                        if (!(data.get("allergens").toString().equals("None"))) {
                                            allergens.add((ArrayList<String>) data.get("allergens"));
                                        } else {
                                            allergens.add(null);
                                        }
                                        price.add(Float.valueOf(data.get("price").toString()));
                                        if (data.get("imageID") != null) {
                                            image.add(data.get("imageID").toString()); // you might want to double check what the name is: image or imageID?
                                        } else {
                                            image.add(null);
                                        }
                                    }
                                }
                            }
                        });
                        for (int i = 0; i < mealName.size(); i++){
                            Meal meal = new Meal(price.get(i), mealName.get(i), description.get(i), mealType.get(i), cuisine.get(i), ingredients.get(i), allergens.get(i));
                            meal.setImageID(image.get(i));
                            meals.add(meal);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                updateView();
                Log.d(TAG, "Second query should be complete");
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MealSearch.this, MealView.class);
        //todo: edit mealview to only show editing elements if user is a cook
        Meal doc = meals.get(position);
        intent.putExtra("MEAL NAME", doc.getMealName());
        intent.putExtra("PRICE", doc.getPrice());
        intent.putExtra("MEAL TYPE", doc.getMealType());
        intent.putExtra("CUISINE", doc.getCuisine());
        intent.putExtra("DESCRIPTION", doc.getDescription());
        intent.putExtra("INGREDIENTS", doc.getIngredients());
        intent.putExtra("ALLERGENS", doc.getAllergens());
        intent.putExtra("IMAGE", doc.getImageID());
        intent.putExtra("OFFERED", doc.getOfferStatus());
        startActivity(intent);
    }
}