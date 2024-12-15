package projet.approche.objet.domain.valueObject.game.exceptions;

public class GameEnded extends GameException {

	public GameEnded() {
		super("The game has already ended");
	}
}
