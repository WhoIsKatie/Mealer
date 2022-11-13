package com.uottawa.seg2105.group10.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Cook extends User{
	private String description, address;
	private double ratingSum;
	private boolean suspended;
	private static final String TAG = "Cook.java";
	private String suspensionEnd;
	private DocumentReference userDoc = null;
	DocumentSnapshot document;

	private int completedOrders, numReviews;
	private Map<String, Meal> cookMenu = new HashMap<>();

	//TODO: implement allergen object/data-type
	//public Hashmap<String, List<Meal>> allergen = new Hashmap<String, List<Meal>>();
	// String (key) is the allergen, List<Meal> for list of meals containing the allergen
	// scrapped this implementation, too complicated. still need a way to filter though right? tbd

	public Cook (DocumentReference userDoc) {
		this.userDoc = userDoc;
		userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					document = task.getResult();
					if (document.exists()) {
						Log.d(TAG, "DocumentSnapshot data: " + document.getData());
						//variables from user super
						firstName = document.getString("firstName");
						lastName = document.getString("lastName");
						email = document.getString("email");
						password = document.getString("password");
						address = document.getString("address");
						type = document.getString("type");
						//variable from inst of this class, Cook.java
						description = document.getString("description");
						suspended = Boolean.TRUE.equals(document.getBoolean("isSuspended"));
						cookMenu = ((HashMap<String, Meal>)document.getData().get("meals"));
					} else {
						Log.d(TAG, "No such document");
					}
				} else {
					Log.d(TAG, "get failed with ", task.getException());
				}
			}
		});

		ratingSum = 0.0;
		completedOrders = 0;
		numReviews = 0;
	}

	// Dummy constructor for testing :)
	public Cook() {
		super("", "", "", "", "Cook");
	}

	public double getRating() {return ratingSum/completedOrders;}
	public void setSuspended(){suspended = true;}
	public boolean addRating(double x) {
		if (x > 5 || x < 0) {
			return false;
		}
		ratingSum += x;
		numReviews++;
		return true;
	}

	public void createMeal(String name, float price, String description, String mealType, String cuisine, HashSet<String> ingredients, HashSet<String> allergens, String image, String document) {
		Meal meal = new Meal(document, price, name, description, mealType, cuisine, ingredients, allergens, image);
		cookMenu.put(name, meal);
		userDoc.update("meals", cookMenu);
	}

	public boolean isSuspended() {return suspended;}

	public String getSuspensionEnd() {return suspensionEnd;}

	public boolean addSuspension(Duration length) {
		if (length != null) {
			if (length.isZero() || length.isNegative()) length = null;
			if (suspensionEnd != null)
				suspensionEnd = LocalDateTime.parse(suspensionEnd).plus(length).toString();
			else {
				if (length != null && suspended == false)
					suspensionEnd = LocalDateTime.now().plus(length).toString();
				else suspensionEnd = null;
			}

		} else suspensionEnd = null;
		suspended = true;
		return true;
	}

}