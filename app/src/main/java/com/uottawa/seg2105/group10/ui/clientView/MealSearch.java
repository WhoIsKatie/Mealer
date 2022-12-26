package com.uottawa.seg2105.group10.ui.clientView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.uottawa.seg2105.group10.R;
import com.uottawa.seg2105.group10.repositories.Meal;
import com.uottawa.seg2105.group10.ui.MealView;
import com.uottawa.seg2105.group10.ui.recyclers.Meal_RecyclerViewAdapter;
import com.uottawa.seg2105.group10.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;

public class MealSearch extends AppCompatActivity implements RecyclerViewInterface {

    private MealSearchViewModel model;
    private static final String TAG = "MealSearch";
    RecyclerView recyclerView;
    private TextInputLayout mealNameLayout, cuisineLayout, typeLayout;
    private TextInputEditText mealEditText;
    private AutoCompleteTextView cuisineSpinner, typeSpinner;
    public String mealNameQuery, cuisineQuery, typeQuery;
    public ArrayList<Meal> searchedMeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_search);
        model = new ViewModelProvider(this).get(MealSearchViewModel.class);

        ImageButton searchButt = findViewById(R.id.mealSearchButt);
        mealNameLayout = findViewById(R.id.nameSearch);
        cuisineLayout = findViewById(R.id.cuisineFilterContainer);
        typeLayout = findViewById(R.id.typeFilterContainer);
        mealEditText = findViewById(R.id.nameSearchEditText);
        cuisineSpinner = findViewById(R.id.cuisineFilterSpinner);
        typeSpinner = findViewById(R.id.typeFilterSpinner);
        recyclerView = findViewById(R.id.mealsRecyclerView);
        searchedMeals = new ArrayList<>();

        setUpDropDownMenus();

        searchButt.setOnClickListener(view -> {
            mealNameQuery = mealEditText.getText().toString();
            cuisineQuery = cuisineSpinner.getText().toString();
            typeQuery = typeSpinner.getText().toString();

            setUpMealModels();
            if(searchedMeals.isEmpty()){
                Toast.makeText(MealSearch.this, "Sorry, no results were found.", Toast.LENGTH_SHORT).show();
            }
            updateView();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchedMeals.clear();
        setUpMealModels();
    }

    /**
     * There is currently a bug involving the exposed drop-down menu wherein only one element
     * will display after selection or any other change in state. I will need to find a work-around
     * this eventually.
     */
    private void setUpDropDownMenus() {
        String[] cuisines = {"Korean", "Thai", "Asian", "African", "Mexican", "Greek",
                "Middle Eastern", "Vietnamese", "Caribbean", "Japanese", "Italian", "Jamaican",
                "Lebanese", "American", "Mediterranean", "Western", "French", "Chinese"};
        String[] mealTypes = {"Breakfast & Brunch", "Snack Food", "Lunch", "Dinner", "Dessert", "Beverage"};
        ArrayAdapter<String> cuisineAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, cuisines);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, mealTypes);

        cuisineSpinner.setAdapter(cuisineAdapter);
        typeSpinner.setAdapter(typeAdapter);
        cuisineSpinner.setDropDownBackgroundDrawable(
                ResourcesCompat.getDrawable(getResources(), R.drawable.filter_spinner_dropdown, null));
        typeSpinner.setDropDownBackgroundDrawable(
                ResourcesCompat.getDrawable(getResources(), R.drawable.filter_spinner_dropdown, null));
    }

    private void updateView(){
        Meal_RecyclerViewAdapter adapter = new Meal_RecyclerViewAdapter("Client",this, searchedMeals, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setUpMealModels(){
        searchedMeals.clear();
        model.getQueryMeals(mealNameQuery, cuisineQuery, typeQuery).observe(MealSearch.this, list -> {
            searchedMeals.addAll(list);
            updateView();
        });
        mealNameQuery = "";
        cuisineQuery = "";
        typeQuery = "";
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MealSearch.this, MealView.class);
        Meal doc = searchedMeals.get(position);
        intent.putExtra("MEAL NAME", doc.getMealName());
        intent.putExtra("PRICE", doc.getPrice());
        intent.putExtra("MEAL TYPE", doc.getMealType());
        intent.putExtra("CUISINE", doc.getCuisine());
        intent.putExtra("DESCRIPTION", doc.getDescription());
        intent.putExtra("INGREDIENTS", doc.getIngredients());
        intent.putExtra("ALLERGENS", doc.getAllergens());
        intent.putExtra("IMAGE", doc.getImageID());
        intent.putExtra("OFFERED", doc.getOfferStatus());
        intent.putExtra("COOKUID", doc.getCookUID());
        startActivity(intent);
    }
}