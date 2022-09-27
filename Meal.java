//import java.util.ArrayList;

public class Meal {
	//private Cook cook;
	// is it necessary to keep a reference to the cook if all the meals enter a menu which is part of the cook class?
	private float price;
	private String name, description, mealType, cuisine, ingredients, allergens;
	public boolean offered = true;
	// by default the meal is offered, need method to change this

	public Meal(String name, float price) {
		this.name = name;
		this.price = price;
		description = mealType = cuisine = ingredients = allergens = "TBD";
	}
	
	public Meal(String name, float price, String description, String mealType, String cuisine, String ingredients, String allergens) {
		// should we keep the constructor this long?? or just make setters?
		this.name = name;
		this.price = price;
		this.description = description;
		this.mealType = mealType;
		this.cuisine = cuisine;
		this.ingredients = ingredients;
		this.allergens = allergens;
	}

	public void offerMeal(){offered = true;}
	public void stopOffering() {offered = false;}
	public boolean isOffered() {return offered;}

	//getters
	public String getName() {return name;}
	public String getDescription() {return description;}
	public String getMealType() {return mealType;}
	public String getCuisine() {return cuisine;}
	public String getIngredients() {return ingredients;}
	public String getAllergens() {return allergens;}
	public float getPrice() {return price;}
	//setters

	public void setName(String name) {this.name = name;}
	public void setPrice(float price) {this.price = price;}
	public void setDescription(String description) {this.description = description;}
	public void setMealType(String type) {this.mealType = type;}
	public void setCuisine(String cuisine) {this.cuisine = cuisine;}
	public void setIngredients(String ingredients) {this.ingredients = ingredients;}
	public void setAllergens(String allergens) {this.allergens = allergens;}
}