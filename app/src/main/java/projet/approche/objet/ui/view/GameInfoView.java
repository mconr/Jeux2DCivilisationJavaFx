package projet.approche.objet.ui.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameInfoView extends StackPane {

	public GameInfoView(Stage stage, ImageView imageView) {
		BorderPane pane = new BorderPane();

		VBox titleBox = new VBox();
		titleBox.setAlignment(javafx.geometry.Pos.CENTER);
		Label title = new Label("Village Manager");
		title.setFont(new Font(30));
		title.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6);-fx-padding: 7;");
		title.setPadding(new Insets(10));
		BorderPane.setAlignment(title, javafx.geometry.Pos.CENTER);
		Label goal = new Label(
				"The goal of the game is to manage resources, inhabitants and buildings, all while trying to make your village prosper.");
		goal.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-padding: 7;");
		goal.setWrapText(true);
		VBox text = new VBox();
		titleBox.getChildren().addAll(title, goal);
		pane.setTop(titleBox);

		Label resourcesTitle = new Label(
				"Resources:");
		resourcesTitle.setFont(new Font(20));
		resourcesTitle.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-padding: 7;");
		Label resources = new Label(
				"There are 10 different resources available in the game. Each resource is used to build buildings or to feed your inhabitants.");
		resources.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-padding: 7;");
		resources.setWrapText(true);

		Label buildingsTitle = new Label(
				"Buildings:");
		buildingsTitle.setFont(new Font(20));
		buildingsTitle.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-padding: 7;");

		Label buildings = new Label(
				"There are 9 types of buildings available in the game. Each building has a cost in resources and a number of workers required to be built. Buildings can be built on empty tiles.");
		buildings.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-padding: 7;");
		buildings.setWrapText(true);

		Label howToPlayTitle = new Label(
				"How To Play:");
		howToPlayTitle.setFont(new Font(20));
		howToPlayTitle.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-padding: 7;");
		Label howToPlay = new Label(
				"You will have to manage your village to try and make it as profitable as possible. If you have the necessary resources, you can add new buildings on empty tiles. Each inhabitant will consume 1 food per day. But be careful, if you run out of food, it's GAME OVER!");
		howToPlay.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-padding: 7;");
		howToPlay.setWrapText(true);

		LevelButtons levelButtons = new LevelButtons(stage);

		text.getChildren().addAll(resourcesTitle, resources, buildingsTitle, buildings, howToPlayTitle,
				howToPlay, levelButtons);
		text.setAlignment(javafx.geometry.Pos.CENTER);
		text.setPadding(new Insets(30));
		pane.setCenter(text);
		this.getChildren().addAll(imageView, pane);
	}

}
