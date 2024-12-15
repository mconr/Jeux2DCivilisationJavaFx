package projet.approche.objet.ui.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projet.approche.objet.application.App;
import projet.approche.objet.ui.Main;

public class GameOverView extends StackPane {

	private BorderPane pane = new BorderPane();

	private Label title = new Label("Game Over");
	private Label score = new Label("Your score is: ");
	private Label daySurvived = new Label("You survived: ");
	private VBox text = new VBox();
	private ImageView imageView = new ImageView(new Image("images/gameOver.png"));

	public GameOverView(Stage stage, App app) {

		this.setScore(app.getScore());
		this.setDaySurvived(app.getDay());

		title.setStyle(
				"-fx-background-color: rgba(255, 255, 255, 0.6);-fx-padding: 7;-fx-font-size: 30px;");
		title.setPrefWidth(500);
		title.setPrefHeight(50);
		title.setTranslateY(-100);
		title.setTranslateX(0);
		title.setWrapText(true);

		score.setStyle(
				"-fx-background-color: rgba(255, 255, 255, 0.6);-fx-padding: 7;-fx-font-size: 30px;");
		score.setPrefWidth(500);
		score.setPrefHeight(50);
		score.setTranslateY(-50);
		score.setTranslateX(0);
		score.setWrapText(true);

		daySurvived.setStyle(
				"-fx-background-color: rgba(255, 255, 255, 0.6);-fx-padding: 7;-fx-font-size: 30px;");
		daySurvived.setPrefWidth(500);
		daySurvived.setPrefHeight(50);
		daySurvived.setTranslateY(0);
		daySurvived.setTranslateX(0);
		daySurvived.setWrapText(true);

		stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.R), () -> {
			Main.restart(stage);
		});

		stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q), () -> {
			stage.close();
		});

		text.getChildren().addAll(title, score, daySurvived);

		text.setAlignment(javafx.geometry.Pos.CENTER);
		text.setPadding(new javafx.geometry.Insets(50));
		pane.setCenter(text);
		this.getChildren().addAll(imageView, pane);
	}

	public void setScore(int score) {
		this.score.setText("Your score is: " + score);
	}

	public void setDaySurvived(int day) {
		this.daySurvived.setText("You survived: " + day + " days");
	}

}
