import java.util.HashMap;

public class Cook extends User{
	private String description;
	private double averageRating, sum;
	private boolean suspended = false;
	// private voidCheque
	private int completedOrders;
	//public Hashmap<String, List<Meal>> allergen = new Hashmap<String, List<Meal>>();
	// String (key) is the allergen, List<Meal> for list of meals containing the allergen
	
	public Cook(String firstName, String lastName, String email, String password, String address, String description) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.description = description;
		
		averageRating = 0.0;
		completedOrders = 0; 
	}
	
	public double getRating() {
		return averageRating;
	}
	
	public void addRating(double x) {
		sum += x;
		
	}
	
	public void createMeal(Cook cook, float price, String description, String mealType, String cuisine, String ingredients, String allergens) {
		Meal meal = new meal(cook, price, description, mealType, cuisine, ingredients, allergens);
	}
}