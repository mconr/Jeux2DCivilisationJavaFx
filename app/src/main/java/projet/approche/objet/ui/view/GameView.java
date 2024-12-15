package projet.approche.objet.ui.view;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GameOverException;
import projet.approche.objet.domain.valueObject.game.exceptions.GamePaused;
import projet.approche.objet.ui.view.infoBar.InfoBar;

public class GameView extends BorderPane {
	private final PickerView pickerView;
	private final GridView gridView;
	private final InfoBar infoBar;
	private AnimationTimer gameLoop;
	private Timer timer = new Timer(1500);

	public GameView(Stage stage, App app) {
		// Tile picker
		this.pickerView = new PickerView(app);
		this.setTop(pickerView);
		// Info bar
		this.infoBar = new InfoBar(this, app);
		this.infoBar.update();
		this.setRight(infoBar);
		// Grid

		List<Updateable> updateables = new ArrayList<>();
		updateables.add(infoBar);
		updateables.add(pickerView);

		this.gridView = new GridView(app, pickerView, updateables);
		this.setCenter(gridView);

		updateables.add(gridView);

		// Update loop
		this.gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (timer.isRunning()) {
					timer.update(now);
				} else {
					timer.start();
					try {
						app.update();
						updateables.forEach(Updateable::update);
					} catch (GameNotStarted | GamePaused | GameEnded e) {
						stop();
					} catch (GameOverException e) {
						GameOverView gameOverView = new GameOverView(stage, app);
						stage.setScene(new Scene(gameOverView));
						stage.sizeToScene();
						stage.show();
						stop();
					}
				}
			}
		};
	}

	public void start() {
		this.gameLoop.start();
	}

	public void stop() {
		this.gameLoop.stop();
	}
}
