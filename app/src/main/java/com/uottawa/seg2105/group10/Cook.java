package com.uottawa.seg2105.group10;

public class Cook extends User{
	// question: should these be made local variables? also what would that look like
	private String description;
	private double ratingSum;
	private boolean suspended = false;
	// private voidCheque
	// how to do images!!!
	private int completedOrders, numReviews;
	private Menu cookMenu = new Menu(this);
	//public Hashmap<String, List<Meal>> allergen = new Hashmap<String, List<Meal>>();
	// String (key) is the allergen, List<Meal> for list of meals containing the allergen
	// scrapped this implementation, too complicated. still need a way to filter though right? tbd
	
	public Cook(String firstName, String lastName, String email, String password, String address, String description) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.description = description;
		
		ratingSum = 0.0;
		completedOrders = 0;
		numReviews = 0;
	}
	
	public double getRating() {return ratingSum/completedOrders;}

	public void addRating(double x) {
		ratingSum += x;
		numReviews++;
	}
	
	public void createMeal(String name, float price, String description, String mealType, String cuisine, String ingredients, String allergens) {
		Meal meal = new Meal(name, price, description, mealType, cuisine, ingredients, allergens);
		this.cookMenu.menu.add(meal);
	}

}