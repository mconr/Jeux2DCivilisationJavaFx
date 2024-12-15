package projet.approche.objet.ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.ui.view.imageResource.BuildingImageResource;

public class PickerView extends HBox implements Updateable {
	private final ToggleGroup group = new ToggleGroup();
	private final ColorAdjust colorAdjust = new ColorAdjust();
	private final App app;

	public PickerView(App app) {
		this.colorAdjust.setSaturation(-1);
		this.setSpacing(15);
		this.setPadding(new Insets(15));
		this.app = app;
		this.update();
	}

	public String getSelected() {
		Toggle toggle = group.getSelectedToggle();
		if (toggle == null)
			return null;
		return (String) toggle.getUserData();
	}

	public void clearSelection() {
		group.selectToggle(null);
	}

	public void update() {
		getChildren().clear();
		for (String type : app.getBuildingsTypes()) {
			ToggleButton btn = new ToggleButton();
			btn.setToggleGroup(group);
			btn.setUserData(type);
			Image image = BuildingImageResource.get(type);
			ImageView imageView = new ImageView(image);
			if (!app.isBuildingAffordable(type))
				imageView.setEffect(colorAdjust);
			btn.setGraphic(imageView);

			// Add text when hovering
			String info = BuildingType.valueOf(type).getStats();
			Tooltip tooltip = new Tooltip(info);
			btn.setOnMouseEntered(event -> {
				Point2D p = btn.localToScreen(btn.getLayoutBounds().getMaxX(), btn.getLayoutBounds().getMaxY());
				tooltip.show(imageView, p.getX(), p.getY());
			});
			btn.setOnMouseExited(event -> {
				tooltip.hide();
			});

			getChildren().add(btn);
		}
	}
}
