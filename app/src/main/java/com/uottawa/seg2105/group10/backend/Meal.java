package com.uottawa.seg2105.group10.backend;//import java.util.ArrayList;

import java.util.HashSet;

public class Meal {
	//private Cook cook;
	private float price;
	private String mealName, description, mealType, cuisine, docID;
	HashSet<String> ingredients, allergens;
	String imageID;
	public boolean offered = true;
	// by default the meal is offered

	public Meal(float price) {
		this.price = price;
		mealName = description = mealType = cuisine = "TBD";
		ingredients = allergens = null;
		imageID = null;
	}
	
	public Meal(String docID, float price, String mealName, String description, String mealType, String cuisine, HashSet<String> ingredients, HashSet<String> allergens, String imageID) {
		// should we keep the constructor this long?? or just make setters?
		// imo there's nothing wrong with it. You can also call a helper method for formatting your class variables :3
		this.price = price;
		this.description = description;
		this.mealType = mealType;
		this.cuisine = cuisine;
		this.ingredients = ingredients;
		this.allergens = allergens;
		this.mealName = mealName;
		this.imageID = imageID;
		this.docID = docID;
	}

	public void offerMeal(){offered = true;}
	public void stopOffering() {offered = false;}
	public boolean isOffered() {return offered;}

	//getters
	public String getMealName(){return mealName;}
	public String getDescription() {return description;}
	public String getMealType() {return mealType;}
	public String getCuisine() {return cuisine;}
	public HashSet<String> getIngredients() {return ingredients;}
	public HashSet<String> getAllergens() {return allergens;}
	public float getPrice() {return price;}
	public String getImageID(){return imageID;}
	public String getDocID(){return docID;}

	//setters
	public void setMealName(String mealName){this.mealName = mealName;}
	public void setPrice(float price) {this.price = price;}
	public void setDescription(String description) {this.description = description;}
	public void setMealType(String type) {this.mealType = type;}
	public void setCuisine(String cuisine) {this.cuisine = cuisine;}
	public void setIngredients(HashSet<String> ingredients) {this.ingredients = ingredients;}
	public void setAllergens(HashSet<String> allergens) {this.allergens = allergens;}
	public void setImage(String imageID){this.imageID = imageID;}
	public void setDocID(String docID){this.docID = docID;}
}