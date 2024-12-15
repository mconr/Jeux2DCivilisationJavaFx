package projet.approche.objet.ui.view.infoBar;

import java.io.IOException;

import javafx.geometry.Insets;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;
import projet.approche.objet.infrastructure.Infrastructure;
import projet.approche.objet.ui.view.GameView;
import projet.approche.objet.ui.view.Updateable;
import projet.approche.objet.ui.view.imageResource.ButtonImageResource;

public class InfoBar extends BorderPane implements Updateable {

	private final Inventory inventory;
	private final Button playPauseButton;
	private final App app;
	private Text day = new Text();
	private Text score = new Text();
	private VBox dayScore = new VBox();

	public InfoBar(GameView gv, App app) {
		this.inventory = new Inventory(app);
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(15);
		buttonBox.setPadding(new Insets(10));
		this.playPauseButton = new Button(gv, app);
		ImageView saveButton = new ImageView(ButtonImageResource.SAVE.getImage());

		saveButton.setOnMouseClicked(e -> {
			Infrastructure is = new Infrastructure();
			try {
				is.save(app);
			} catch (NoBuildingHereException | NotInGridException | IOException e1) {
				e1.printStackTrace();
			}
		});

		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-0.4);
		saveButton.setOnMouseEntered(e -> {
			saveButton.setEffect(colorAdjust);
		});
		saveButton.setOnMouseExited(e -> {
			saveButton.setEffect(null);
		});

		buttonBox.getChildren().addAll(playPauseButton, saveButton);
		day.setFill(javafx.scene.paint.Color.BLACK);
		day.setFont(new Font(20));
		day.getStyleClass().add("number");
		day.setCache(true);

		score.setFill(javafx.scene.paint.Color.BLACK);
		score.setFont(new Font(20));
		score.getStyleClass().add("number");
		score.setCache(true);

		this.dayScore.getChildren().addAll(day, score);
		this.dayScore.setAlignment(javafx.geometry.Pos.TOP_LEFT);
		this.dayScore.setSpacing(20);

		this.app = app;
		this.setRight(inventory);
		this.setLeft(dayScore);
		this.setTop(buttonBox);
	}

	public void update() {
		this.inventory.update();
		this.playPauseButton.update();
		this.updateDay();
		this.updateScore();
	}

	public void updateDay() {
		day.setText("   Day " + app.getDay());
	}

	public void updateScore() {
		score.setText("   Score: " + app.getScore());
	}
}
