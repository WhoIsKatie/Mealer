package com.uottawa.seg2105.group10.backend;//import java.util.ArrayList;

import java.util.ArrayList;
import java.util.HashMap;

public class Meal {
	//private Cook cook;
	private float price;
	private String mealName, description, mealType, docID;
	ArrayList<String> cuisine;
	HashMap<String, String> ingredients, allergens;
	String imageID;
	public boolean offered = true;
	// by default the meal is offered

	public Meal(float price) {
		this.price = price;
		imageID = mealName = description = mealType = "TBD";
		ingredients = new HashMap<>();
		allergens = new HashMap<>();
	}

	// I've modified Meal so that the docID of each meal on the database is the mealName => a cook cannot have two meals with the same name - Katie
	public Meal(float price, String mealName, String description, String mealType, ArrayList<String> cuisine, HashMap<String, String> ingredients, HashMap<String, String> allergens) {
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
	}


	public void offerMeal(){offered = true;}
	public void stopOffering() {offered = false;}
	public boolean isOffered() {return offered;}

	//getters
	public String getMealName(){return mealName;}
	public String getDescription() {return description;}
	public String getMealType() {return mealType;}
	public ArrayList<String> getCuisine() {return cuisine;}
	public HashMap<String, String> getIngredients() {return ingredients;}
	public HashMap<String, String> getAllergens() {return allergens;}
	public float getPrice() {return price;}
	public String getImageID(){return imageID;}
	public String getDocID(){return docID;}

	//setters
	public void setMealName(String mealName){this.mealName = mealName;}
	public void setPrice(float price) {this.price = price;}
	public void setDescription(String description) {this.description = description;}
	public void setMealType(String type) {this.mealType = type;}
	public void setCuisine(ArrayList<String> cuisine) {this.cuisine = cuisine;}
	public void setIngredients(HashMap<String, String> ingredients) {this.ingredients = ingredients;}
	public void setAllergens(HashMap<String, String> allergens) {this.allergens = allergens;}
	public void setImageID(String imageID){this.imageID = imageID;}
	public void setDocID(String docID){this.docID = docID;}
}