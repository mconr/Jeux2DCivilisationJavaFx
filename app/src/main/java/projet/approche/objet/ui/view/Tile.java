package projet.approche.objet.ui.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends ImageView {

	public Tile(Image image) {
		super(image);
	}

	public Tile(Image image, int x, int y) {
		this(image);
		this.setLayoutX(x);
		this.setLayoutY(y);
	}
}
