package projet.approche.objet.ui.view.infoBar;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.game.exceptions.GameAlreadyStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.ui.view.GameView;
import projet.approche.objet.ui.view.Updateable;
import projet.approche.objet.ui.view.imageResource.ButtonImageResource;

public class Button extends VBox implements Updateable {

	private final App app;
	private HBox button;
	private static final ImageView play = new ImageView(ButtonImageResource.PLAY.getImage());
	private static final ImageView pause = new ImageView(ButtonImageResource.PAUSE.getImage());
	private String lastState = "NOTSTARTED";

	public Button(GameView gv, App app) {
		this.app = app;
		this.button = new HBox();
		this.button.setAlignment(javafx.geometry.Pos.CENTER);
		button.setCache(true);
		button.getChildren().addAll(play);
		button.setOnMouseClicked(e -> {
			switch (app.getGameState()) {
				case "NOTSTARTED":
					try {
						app.startGame();
						gv.start();
					} catch (GameAlreadyStarted | GameEnded e1) {
						throw new RuntimeException(e1); // should not happen
					}
					break;
				case "PAUSED":
					gv.start();
					try {
						app.pauseGame();
					} catch (GameNotStarted | GameEnded e1) {
						throw new RuntimeException(e1); // should not happen
					}
					break;
				case "RUNNING":
					gv.stop();
					try {
						app.pauseGame();
					} catch (GameNotStarted | GameEnded e1) {
						throw new RuntimeException(e1); // should not happen
					}
					break;
				case "ENDED":
					// TODO: restart game
					break;
				default:
					break;
			}
			this.update();
		});

		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-0.4);
		button.setOnMouseEntered(e -> {
			button.setEffect(colorAdjust);
		});
		button.setOnMouseExited(e -> {
			button.setEffect(null);
		});
		getChildren().addAll(button);
	}

	public void update() {
		switch (app.getGameState()) {
			case "NOTSTARTED":
			case "PAUSED":
				if (!lastState.equals("PAUSED")) {
					button.getChildren().clear();
					button.getChildren().addAll(play);
				}
				break;
			case "RUNNING":
				if (!lastState.equals("RUNNING")) {
					button.getChildren().clear();
					button.getChildren().addAll(pause);
				}
				break;
			case "ENDED":
				// TODO: restart game
				break;
			default:
				break;
		}
		this.lastState = app.getGameState();
	}

}
