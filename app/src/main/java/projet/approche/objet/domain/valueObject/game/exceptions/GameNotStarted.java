package projet.approche.objet.domain.valueObject.game.exceptions;

public class GameNotStarted extends GameException {

	public GameNotStarted() {
		super("The game has not started yet");
	}

}
