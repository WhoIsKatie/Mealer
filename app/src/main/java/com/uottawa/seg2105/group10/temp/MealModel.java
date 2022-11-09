package com.uottawa.seg2105.group10.temp;

public class MealModel {

    //TODO: Is this really necessary since we already have a Meal class with the same variables?
    private String mealName, description, mealType, cuisine, ingredients, allergens;
    private double price;

    public MealModel(String mealName, String description, String mealType, String cuisine, String ingredients, String allergens, double price) {
        this.mealName = mealName;
        this.description = description;
        this.mealType = mealType;
        this.cuisine = cuisine;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
    }

    public String getMealName() {
        return mealName;
    }

    public String getDescription() {
        return description;
    }

    public String getMealType() {
        return mealType;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getAllergens() {
        return allergens;
    }

    public double getPrice() {
        return price;
    }
}
