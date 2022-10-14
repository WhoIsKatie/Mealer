package com.uottawa.seg2105.group10.backend;

import androidx.annotation.DrawableRes;

public class Cook extends User{
	// question: should these be made local variables? also what would that look like
	private String description;
	private double ratingSum;
	private boolean suspended = false;

	//TODO: look into setting images as class attributes
	@DrawableRes
	private int voidCheque;

	private int completedOrders, numReviews;
	private Menu cookMenu = new Menu(this);

	//TODO: implement allergen object/data-type
	//public Hashmap<String, List<Meal>> allergen = new Hashmap<String, List<Meal>>();
	// String (key) is the allergen, List<Meal> for list of meals containing the allergen
	// scrapped this implementation, too complicated. still need a way to filter though right? tbd

	public Cook (String firstName, String lastName, String email, String password, String address) {
		super(firstName, lastName, email, password, address, "Cook");
		this.description = description;
		
		ratingSum = 0.0;
		completedOrders = 0;
		numReviews = 0;
	}

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