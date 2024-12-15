package projet.approche.objet.domain.valueObject.game.exceptions;

public class GamePaused extends Exception {
	public GamePaused() {
		super();
	}

	public GamePaused(String message) {
		super(message);
	}
}
