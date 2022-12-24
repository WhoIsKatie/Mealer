package com.uottawa.seg2105.group10.ui.cookView;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uottawa.seg2105.group10.repositories.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CookHomeViewModel extends ViewModel {

    private MutableLiveData<List<Meal>> meals;

    public LiveData<List<Meal>> getMeals() {
        meals = new MutableLiveData<>();
        loadMealsFromFirebase();
        return meals;
    }

    public void loadMealsFromFirebase() {
        FirebaseFirestore dBase = FirebaseFirestore.getInstance();
        ArrayList<Meal> mealList = new ArrayList<>();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference userRef = dBase.collection("users").document(uid);

        // fetch all cook's meals
        userRef.collection("meals").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Log.d("CookHomeViewModel", document.getId() + "=>" + document.getData());
                            Meal m = document.toObject(Meal.class);
                            mealList.add(m);
                        }
                        meals.setValue(mealList);
                    }
                });
    }
}
