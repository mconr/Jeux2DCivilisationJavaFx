package projet.approche.objet.ui.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InputPopup {

	public static void openInputPopup(InputCallback callback) {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);

		TextField textField = new TextField();
		textField.setPromptText("Enter something...");

		// Action for handling the input
		Runnable submitAction = () -> {
			String userInput = textField.getText();
			callback.handleInput(userInput);
			popupStage.close();
		};

		textField.setOnAction(event -> submitAction.run());

		Button submitButton = new Button("Submit");
		submitButton.setOnAction(event -> submitAction.run());

		VBox layout = new VBox(10, textField, submitButton);
		layout.setStyle("-fx-padding: 10;");

		Scene scene = new Scene(layout, 200, 120);
		popupStage.setScene(scene);
		popupStage.setTitle("Input Pop-up");
		popupStage.showAndWait();
	}

}
