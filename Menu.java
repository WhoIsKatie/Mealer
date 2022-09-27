import java.util.List;
import java.util.ArrayList;

public class Menu {
	private final Cook cook;
	public List<Meal> menu;

	public Menu(Cook cook) {
		this.cook = cook;
		this.menu = new ArrayList<>();
	}
}