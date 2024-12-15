package projet.approche.objet.ui.view.imageResource;

import javafx.scene.image.Image;

public enum BuildingImageResource {

	APARTMENTBUILDING("ApartmentBuilding.png"),
	HOUSE("House.png"),
	CEMENTPLANT("CementPlant.png"),
	FARM("Farm.png"),
	LUMBERMILL("LumberMill.png"),
	QUARRY("Quarry.png"),
	STEELMILL("SteelMill.png"),
	TOOLFACTORY("ToolFactory.png"),
	WOODENCABIN("WoodenCabin.png"),
	CONSTRUCTION("Construction.png"),
	EMPTY("Empty.png");

	private final Image image;

	public static final int size = 64;

	BuildingImageResource(String file) {
		try {
			this.image = new Image(BuildingImageResource.class.getResourceAsStream("/images/buildings/" + file));
			if (image.getWidth() != size && image.getHeight() != size) {
				String msg = "File " + file + " does not have the correct size " + image.getWidth() + " x "
						+ image.getHeight();
				throw new RuntimeException(msg);
			}
		} catch (NullPointerException e) {
			System.err.println("Resource not found : " + file);
			throw e;
		}
	}

	public Image getImage() {
		return image;
	}

	public static Image get(String kind) {
		if (kind != null) {
			// remove spaces
			String str = kind.replaceAll("\\s+", "").toUpperCase();
			return BuildingImageResource.valueOf(str).image;
		}
		return null;
	}

}
