package projet.approche.objet.ui.view.infoBar;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import projet.approche.objet.application.App;
import projet.approche.objet.ui.view.Updateable;
import projet.approche.objet.ui.view.imageResource.ResourceImageResource;

public class Inventory extends VBox implements Updateable {
	private final App app;
	private final Text gold = new Text();
	private final Text food = new Text();
	private final Text wood = new Text();
	private final Text stone = new Text();
	private final Text coal = new Text();
	private final Text iron = new Text();
	private final Text steel = new Text();
	private final Text cement = new Text();
	private final Text lumber = new Text();
	private final Text tools = new Text();
	private final Text person = new Text();
	private final Text worker = new Text();

	public Inventory(App app) {
		this.app = app;
		this.setSpacing(5);
		this.setPadding(new Insets(5));

		HBox goldGroup = infoGroup("Gold", this.gold);
		HBox foodGroup = infoGroup("Food", this.food);
		HBox woodGroup = infoGroup("Wood", this.wood);
		HBox stoneGroup = infoGroup("Stone", this.stone);
		HBox coalGroup = infoGroup("Coal", this.coal);
		HBox ironGroup = infoGroup("Iron", this.iron);
		HBox steelGroup = infoGroup("Steel", this.steel);
		HBox cementGroup = infoGroup("Cement", this.cement);
		HBox lumberGroup = infoGroup("Lumber", this.lumber);

		HBox toolsGroup = infoGroup("Tools", this.tools);
		HBox inhabitantsGroup = infoGroup("Inhabitants", this.person);
		HBox workersGroup = infoGroup("Workers", this.worker);

		getChildren().addAll(goldGroup, foodGroup, woodGroup, stoneGroup, coalGroup, ironGroup, steelGroup, cementGroup,
				lumberGroup, toolsGroup, inhabitantsGroup, workersGroup);
	}

	private HBox infoGroup(String kind, Text number) {
		HBox group = new HBox();
		ImageView img = new ImageView(ResourceImageResource.get(kind));
		Tooltip tooltip = new Tooltip(kind);
		img.setOnMouseEntered(event -> {
			Point2D p = img.localToScreen(img.getLayoutBounds().getMaxX(), img.getLayoutBounds().getMaxY());
			tooltip.show(img, p.getX(), p.getY());
		});
		img.setOnMouseExited(event -> {
			tooltip.hide();
		});
		group.setSpacing(4);
		number.setCache(true);
		number.setFill(Color.BLACK);
		number.getStyleClass().add("number");
		group.getChildren().addAll(img, number);
		return group;
	}

	public void update() {
		gold.setText(getResourceQuantity("GOLD"));
		food.setText(getResourceQuantity("FOOD"));
		wood.setText(getResourceQuantity("WOOD"));
		stone.setText(getResourceQuantity("STONE"));
		coal.setText(getResourceQuantity("COAL"));
		iron.setText(getResourceQuantity("IRON"));
		steel.setText(getResourceQuantity("STEEL"));
		cement.setText(getResourceQuantity("CEMENT"));
		lumber.setText(getResourceQuantity("LUMBER"));
		tools.setText(getResourceQuantity("TOOLS"));
		person.setText(Integer.toString(this.app.getInHabitantsInBuildings()) + '['
				+ Integer.toString(this.app.getInhabitantsNumber()) + ']');
		worker.setText(Integer.toString(this.app.getWorkersInBuildings()) + '['
				+ Integer.toString(this.app.getWorkersNumber()) + ']');
	}

	private String getResourceQuantity(String resourceType) {
		String plusSign = "";
		if (this.app.getPureResourceProduction(resourceType) > 0)
			plusSign = "+";
		return Integer.toString(this.app.getResourceQuantity(resourceType)) + " (" + plusSign
				+ Integer.toString(this.app.getPureResourceProduction(resourceType)) + ")";
	}
}
