package projet.approche.objet.ui.view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.game.GameStarter;

public class GameStarterView extends BorderPane {

	public GameStarterView(Stage stage) {
		ImageView imageView = new ImageView(new Image("images/Game.png"));
		GameInfoView gameInfoView = new GameInfoView(stage, imageView);
		this.setCenter(gameInfoView);
		this.prefWidthProperty().bind(imageView.fitWidthProperty());
		this.prefHeightProperty().bind(imageView.fitHeightProperty());
	}

	public static void startGame(Stage stage, GameStarter gs, String title) {
		App app = new App(gs);
		GameView gameView = new GameView(stage, app);
		Scene gameScene = new Scene(gameView);
		stage.setTitle(title);
		stage.setScene(gameScene);
		stage.sizeToScene();
		stage.show();
	}
}
