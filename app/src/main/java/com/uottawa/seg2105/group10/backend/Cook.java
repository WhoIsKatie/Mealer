package com.uottawa.seg2105.group10.backend;

import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class Cook implements User{
	// question: should these be made local variables? also what would that look like
	private String description, firstName, lastName, email, password, address;
	private double ratingSum;
	private boolean suspended = false;
	private DocumentSnapshot document;
	private static final String TAG = "Cook.java";

	//TODO: look into setting images as class attributes
	@DrawableRes
	private int voidCheque;

	private int completedOrders, numReviews;
	private Menu cookMenu = new Menu(this);

	//TODO: implement allergen object/data-type
	//public Hashmap<String, List<Meal>> allergen = new Hashmap<String, List<Meal>>();
	// String (key) is the allergen, List<Meal> for list of meals containing the allergen
	// scrapped this implementation, too complicated. still need a way to filter though right? tbd

	public Cook(){}

	public Cook (DocumentReference userDoc) {
		userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					document = task.getResult();
					if (document.exists()) {
						Log.d(TAG, "DocumentSnapshot data: " + document.getData());
						firstName = document.getString("firstName");
						lastName = document.getString("lastName");
						email = document.getString("email");
						password = document.getString("password");
						address = document.getString("address");
						description = document.getString("description");
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

	public String getFirstName() {return firstName;}
	public String getLastName() {return lastName;}
	protected String getDescription() {return description;}
	protected String getEmail() {return email;}
	protected String getPassword(){return password;}
	public String getAddress(){return address;}
	public String getType(){return "Cook";}

	public boolean completeProfile(String about, int chequeImg) {
		//TODO: if appropriate, set conditions that return FALSE
		voidCheque = chequeImg;
		description = about;
		return true;
	}

	public double getRating() {return ratingSum/completedOrders;}

	public boolean addRating(double x) {
		if (x > 5 || x < 0) {
			return false;
		}
		ratingSum += x;
		numReviews++;
		return true;
	}
	
	public void createMeal(String name, float price, String description, String mealType, String cuisine, String ingredients, String allergens) {
		Meal meal = new Meal(name, price, description, mealType, cuisine, ingredients, allergens);
		this.cookMenu.menu.add(meal);
	}



}