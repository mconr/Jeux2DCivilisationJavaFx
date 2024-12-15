package projet.approche.objet.ui.view.imageResource;

import javafx.scene.image.Image;

public enum ButtonImageResource {

	EASY("Easy.png"),
	NORMAL("Normal.png"),
	HARD("Hard.png"),
	LOAD("Load.png"),
	SAVE("Save.png"),
	PLAY("Play.png"),
	PAUSE("Pause.png");

	private final Image image;

	public static final int width = 100;
	public static final int height = 100;

	ButtonImageResource(String file) {
		try {
			this.image = new Image(ButtonImageResource.class.getResourceAsStream("/images/buttons/" + file));
			if (image.getWidth() != width && image.getHeight() != height) {
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
			return ButtonImageResource.valueOf(str).image;
		}
		return null;
	}

}
