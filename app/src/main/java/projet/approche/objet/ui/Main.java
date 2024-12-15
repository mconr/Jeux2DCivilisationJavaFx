package projet.approche.objet.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projet.approche.objet.ui.view.GameStarterView;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		restart(primaryStage);
	}

	public static void main(String[] args) {
		launch();
	}

	public static void restart(Stage stage) {
		GameStarterView gameStarterView = new GameStarterView(stage);

		stage.setTitle("Approche Objet");
		stage.setScene(new Scene(gameStarterView));
		stage.show();
	}
}
