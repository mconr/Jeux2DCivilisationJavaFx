package projet.approche.objet.domain.valueObject.game.exceptions;

public class GameAlreadyStarted extends GameException {
	public GameAlreadyStarted() {
		super("The game has already started");
	}
}
