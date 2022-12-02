package com.uottawa.seg2105.group10.backend;//import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Meal {
	private float price;
	private String mealName, description, mealType, cookUID;
	ArrayList<String> cuisine;
	ArrayList<String> ingredients, allergens;
	String imageID;
	public boolean offered = true; 				// by default the meal is offered

	protected final FirebaseFirestore dBase = FirebaseFirestore.getInstance();
	protected final static String TAG = "Meal";

	public Meal(float price) {
		this.price = price;
		imageID = mealName = description = mealType = "TBD";
		ingredients = new ArrayList<>();
		allergens = new ArrayList<>();
	}

	public Meal(float price, String mealName, String description, String mealType, ArrayList<String> cuisine, ArrayList<String> ingredients, ArrayList<String> allergens) {
		this.price = price;
		this.description = description;
		this.mealType = mealType;
		this.cuisine = cuisine;
		this.ingredients = ingredients;
		this.allergens = allergens;
		this.mealName = mealName;
	}

	public Meal() {}

	public void offerMeal(){offered = true;}
	public void stopOffering() {offered = false;}

	//getters
	public String getMealName(){return mealName;}
	public String getDescription() {return description;}
	public String getMealType() {return mealType;}
	public ArrayList<String> getCuisine() {return cuisine;}
	public ArrayList<String> getIngredients() {return ingredients;}
	public ArrayList<String> getAllergens() {return allergens;}
	public float getPrice() {return price;}
	public String getImageID(){return imageID;}
	public Boolean getOfferStatus(){return offered;}
	public String getCookUID(){return cookUID;}

	//setters
	public void setDescription(String description) {this.description = description;}
	public void setImageID(String imageID){this.imageID = imageID;}
	public void setCookUID(String cookUID){this.cookUID = cookUID;}

	//make a purchase of this meal (done like this so its easy to do from adapter position)
	public static Purchase createPurchase(String docID, String cookUID, String clientUID, String mealName, LocalDateTime pickTime, String clientName) {
		return new Purchase(docID, cookUID, clientUID, mealName, pickTime.toString(), clientName);
	}

	public boolean updateFirestore(Context c) {
		DocumentReference mealRef = dBase.collection("users").document(cookUID).collection("meals").document(mealName);
		final boolean[] flag = new boolean[1];
		mealRef.set(this).addOnSuccessListener(v -> {
			Log.d(TAG, "Added meal successfully.");
			Toast.makeText(c, "Added meal!", Toast.LENGTH_SHORT).show();
			flag[0] = true;
		}).addOnFailureListener(unused -> {
			Log.d(TAG, "Could not add the meal.");
			Toast.makeText(c, "Could not add the meal.", Toast.LENGTH_SHORT).show();
			flag[0] = false;
		});
		return flag[0];
	}
}