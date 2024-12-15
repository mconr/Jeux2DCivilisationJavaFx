package projet.approche.objet.application;

import projet.approche.objet.domain.valueObject.game.exceptions.GameAlreadyStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GameOverException;
import projet.approche.objet.domain.valueObject.game.exceptions.GamePaused;

public interface GameService {

	public void pauseGame() throws GameNotStarted, GameEnded;

	public void startGame() throws GameEnded, GameAlreadyStarted;

	public void endGame() throws GameNotStarted, GameEnded;

	public void update() throws GameNotStarted, GameEnded, GamePaused, GameOverException;

	public boolean isGameStarted();

	public boolean isGameRunning();

	public boolean isGameEnded();

	public boolean isGamePaused();

	public String getGameState();

	public int getDay();

	public int getGridSize();

	public int getScore();
}
