package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.temp.Meal_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.temp.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Map;


public class Menu extends AppCompatActivity implements RecyclerViewInterface {

    public ArrayList<Meal> meals;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dBase;
    private static final String TAG = "Menu";
    RecyclerView recyclerView;
    private Button addMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        meals = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.mealsRecyclerView);
        setUpMealModels();
    }

    private void updateView(){
        Meal_RecyclerViewAdapter adapter = new Meal_RecyclerViewAdapter(this, meals, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setUpMealModels(){
        // initializing all lists of fields for complaints that are active
        ArrayList<String> mealName = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> mealType = new ArrayList<>();
        ArrayList<String> cuisine = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();
        ArrayList<String> allergens = new ArrayList<>();
        ArrayList<Float> price = new ArrayList<>();
        ArrayList<Integer> image = new ArrayList<>();
        ArrayList<String> documents = new ArrayList<>();

        // used official docs: https://firebase.google.com/docs/firestore/query-data/queries#simple_queries
        dBase.collection("meals").whereEqualTo("status", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            // todo: cannot use whereEqualTo because meals dont have this field; consider using boolean offered (eg. whereEqualTo("offered", true))
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                    Log.d(TAG, document.getId() + "=>" + document.getData());
                    Map<String, Object> data = document.getData();
                    mealName.add(data.get("mealName").toString());
                    description.add(data.get("description").toString());
                    mealType.add(data.get("mealType").toString());
                    cuisine.add(data.get("cuisine").toString());

                    //todo: @JACOB ingredients and allergens are hashsets so this approach doesn't work; when creating a meal u need to put hashsets into meal's constructor for both ingred + allergen
                    // todo: image is actually imageID and it is a string
                    ingredients.add(data.get("ingredients").toString());
                    allergens.add(data.get("allergens").toString());
                    price.add(Float.valueOf(data.get("price").toString()));
                    image.add(Integer.valueOf(data.get("image").toString()));
                    documents.add(document.getReference().getId());
                }
                for (int i = 0; i < mealName.size(); i++){
                    Meal meal = new Meal(documents.get(i), price.get(i), mealName.get(i), description.get(i), mealType.get(i), cuisine.get(i), ingredients.get(i), allergens.get(i), image.get(i));
                    meals.add(meal);
                }
                updateView();
            }
        });

        addMeal = findViewById(R.id.addMeal);
        addMeal.setOnClickListener(view -> {
            startActivity(new Intent(Menu.this, AddMeal.class));
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Menu.this, MealView.class);
        Meal doc = meals.get(position);
        intent.putExtra("DOCUMENT", doc.getDocID());
        intent.putExtra("PRICE", doc.getPrice());
        intent.putExtra("MEAL NAME", doc.getMealName());
        intent.putExtra("DESCRIPTION", doc.getDescription());
        intent.putExtra("MEAL TYPE", doc.getMealType());
        intent.putExtra("CUISINE", doc.getCuisine());
        intent.putExtra("INGREDIENTS", doc.getIngredients());
        intent.putExtra("ALLERGENS", doc.getAllergens());
        intent.putExtra("IMAGE", doc.getImageID());
        startActivity(intent);
    }
}
