package com.uottawa.seg2105.group10.ui.clientView;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.uottawa.seg2105.group10.repositories.Meal;

import java.util.ArrayList;
import java.util.List;

public class MealSearchViewModel extends ViewModel {

    private MutableLiveData<List<Meal>> queryResults;
    private String name, cuisine, type;

    public LiveData<List<Meal>> getQueryMeals(String name, String cuisine, String type) {
        queryResults = new MutableLiveData<>();
        this.name = name;
        this.cuisine = cuisine;
        this.type = type;
        loadQueryMeals();
        return queryResults;
    }

    private void loadQueryMeals() {
        FirebaseFirestore dBase = FirebaseFirestore.getInstance();
        ArrayList<Meal> meals = new ArrayList<>();
        Query cookQuery = dBase.collection("users").whereEqualTo("type", "Cook");
        cookQuery.get().addOnSuccessListener(cookQuerySnapshots -> {
            for (DocumentSnapshot cook : cookQuerySnapshots.getDocuments()) {
                if (Boolean.TRUE.equals(cook.getBoolean("suspended"))) {
                    continue;
                }
                Query searchQuery = cook.getReference().collection("meals")
                        .whereEqualTo("offerStatus", true);
                if (name != null)
                    if (!name.isEmpty()) searchQuery = searchQuery.whereEqualTo("mealName", name);
                if (cuisine != null)
                    if (!cuisine.isEmpty()) searchQuery = searchQuery.whereArrayContains("cuisine", cuisine);
                if (type != null)
                    if (!type.isEmpty()) searchQuery = searchQuery.whereEqualTo("mealType", type);

                searchQuery.get().addOnSuccessListener(querySnapshots -> {
                    for (DocumentSnapshot meal : querySnapshots.getDocuments()) {
                        Log.d("MealSearchViewModel", meal.getId() + "=>" + meal.getData());
                        Meal m = meal.toObject(Meal.class);
                        meals.add(m);
                    }
                    Log.d("MealSearchViewModel", Integer.toString(meals.size()));
                    queryResults.setValue(meals);
                });
            }
        });
    }
}
