package projet.approche.objet.ui.view.imageResource;

import javafx.scene.image.Image;

public enum ResourceImageResource {

	GOLD("Gold.png"),
	FOOD("Food.png"),
	WOOD("Wood.png"),
	STONE("Stone.png"),
	COAL("Coal.png"),
	IRON("Iron.png"),
	STEEL("Steel.png"),
	CEMENT("Cement.png"),
	LUMBER("Lumber.png"),
	TOOLS("Tools.png"),
	INHABITANTS("Inhabitants.png"),
	WORKERS("Workers.png");

	private final Image image;

	public static final int size = 32;

	ResourceImageResource(String file) {
		try {
			this.image = new Image(ResourceImageResource.class.getResourceAsStream("/images/resources/" + file));
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
			return ResourceImageResource.valueOf(str).image;
		}
		return null;
	}

}
