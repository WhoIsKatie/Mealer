package com.uottawa.seg2105.group10.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.backend.Meal;
import com.uottawa.seg2105.group10.recyclers.Meal_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.recyclers.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Map;


public class Menu extends AppCompatActivity implements RecyclerViewInterface {

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
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        dBase = FirebaseFirestore.getInstance();
        String userUID = mAuth.getCurrentUser().getUid();
        userRef = dBase.collection("users").document(userUID);

        recyclerView = findViewById(R.id.mealsRecyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpMealModels();
    }

    private void updateView(){
        Meal_RecyclerViewAdapter adapter = new Meal_RecyclerViewAdapter(this, meals, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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

        userRef.collection("meals").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                Log.d(TAG, document.getId() + "=>" + document.getData());
                Map<String, Object> data = document.getData();
                mealName.add(data.get("mealName").toString());
                description.add(data.get("description").toString());
                mealType.add(data.get("mealType").toString());
                if(!(data.get("cuisine").toString().equals("None"))){
                    cuisine.add((ArrayList<String>) data.get("cuisine"));
                }
                else{
                    cuisine.add(null);
                }
                documents.add(document.getId());

                if(! (data.get("ingredients").toString().equals("None"))){
                    ingredients.add((ArrayList<String>) data.get("ingredients"));
                }
                else{
                    ingredients.add(null);
                }
                if(! (data.get("allergens").toString().equals("None"))){
                    allergens.add((ArrayList<String>) data.get("allergens"));
                }
                else{
                    allergens.add(null);
                }
                price.add(Float.valueOf(data.get("price").toString()));
                if(data.get("imageID") != null){
                    image.add(data.get("imageID").toString()); // you might want to double check what the name is: image or imageID?
                }
                else {
                    image.add(null);
                }
            }
            for (int i = 0; i < mealName.size(); i++){
                Meal meal = new Meal(price.get(i), mealName.get(i), description.get(i), mealType.get(i), cuisine.get(i), ingredients.get(i), allergens.get(i));

                meal.setImageID(image.get(i));
                meals.add(meal);
            }
            updateView();
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
