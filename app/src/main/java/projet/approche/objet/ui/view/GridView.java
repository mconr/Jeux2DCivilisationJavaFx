package projet.approche.objet.ui.view;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseButton;

import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.building.exceptions.BuildingAlreadyStartedException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotBuiltException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotEnoughNeedsException;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.NoMoreSpace;
import projet.approche.objet.domain.valueObject.game.exceptions.NotEnoughInhabitants;
import projet.approche.objet.domain.valueObject.game.exceptions.NotEnoughWorkers;
import projet.approche.objet.domain.valueObject.grid.Coordinate;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotFreeException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;
import projet.approche.objet.ui.view.imageResource.BuildingImageResource;

public class GridView extends BorderPane implements Updateable {
	private final PickerView pickerView;
	private final int gridSize;
	private final App app;
	private final List<Updateable> updateables;
	private final ContextMenu contextMenu = new ContextMenu();
	private int x;
	private int y;

	public GridView(App app, PickerView pickerView, List<Updateable> updateables) {
		this.pickerView = pickerView;
		this.app = app;
		this.gridSize = app.getGridSize();
		this.updateables = updateables;
		createContextMenu();
		setPrefSize(gridSize * BuildingImageResource.size,
				gridSize * BuildingImageResource.size);
		update();
	}

	private void createTile(int i, int j) throws NoBuildingHereException, NotInGridException {
		int layoutX = i * BuildingImageResource.size;
		int layoutY = j * BuildingImageResource.size;
		String kind;
		try {
			kind = app.getBuildingType(i, j);
		} catch (NoBuildingHereException e) {
			kind = "Empty";
		} catch (NotInGridException e) {
			throw new RuntimeException(e); // should not happen
		}
		Tile tile = new Tile(BuildingImageResource.get(kind), layoutX, layoutY);
		getChildren().add(tile);
		tile.setOnMouseClicked(e -> {
			x = i;
			y = j;
			if (e.getButton() == MouseButton.SECONDARY) {
				contextMenu.show(tile, e.getScreenX(), e.getScreenY());
			} else {
				try {
					update(tile, i, j);
				} catch (NoBuildingHereException | NotInGridException e1) {
					e1.printStackTrace();
				}
				updateables.forEach(updateable -> {
					if (updateable != this)
						updateable.update();
				});
			}
		});
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-0.2);
		tile.setOnMouseEntered(e -> {
			tile.setEffect(colorAdjust);
		});
		tile.setOnMouseExited(e -> {
			tile.setEffect(null);
		});

		if (kind != "Empty") {
			String info = app.getManager().getGrid().getBuilding(new Coordinate(i, j)).toString();
			Tooltip tooltip = new Tooltip(info);
			tile.setOnMouseEntered(event -> {
				Point2D p = tile.localToScreen(tile.getLayoutBounds().getMaxX(), tile.getLayoutBounds().getMaxY());
				tooltip.show(tile, p.getX(), p.getY());
			});
			tile.setOnMouseExited(event -> {
				tooltip.hide();
			});
		}

	}

	private void update(Tile tile, int i, int j)
			throws NoBuildingHereException, NotInGridException {
		String kind;
		try {
			kind = app.getBuildingType(i, j);
		} catch (NoBuildingHereException e) {
			kind = null;
		} catch (NotInGridException e) {
			throw new RuntimeException(e); // should not happen
		}
		if (pickerView.getSelected() != null && !pickerView.getSelected().equals(kind)) {
			getChildren().remove(tile);
			makePopUpForException(() -> app.buildBuilding(pickerView.getSelected(), i, j));
			createTile(i, j);
		}
	}

	public void update() {
		getChildren().clear();
		for (int i = 0; i < this.gridSize; i++) {
			for (int j = 0; j < this.gridSize; j++) {
				try {
					createTile(i, j);
				} catch (NoBuildingHereException | NotInGridException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int getSize() {
		return this.getSize();
	}

	private void popUp(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void makePopUpForException(RunnableWithException method) {
		try {
			method.run();
		} catch (GameNotStarted e) {
			this.popUp("Game not started", "Game not started...",
					"You must start the game before building.");
		} catch (GameEnded e) {
			this.popUp("Game ended", "Game ended...",
					"You must start a new game before building.");
		} catch (NotInGridException e) {
			this.popUp("Not in grid", "Not in grid...",
					"You must build in the grid.");
		} catch (NotFreeException e) {
			this.popUp("Tile not free", "Tile not free...",
					"You must build on a free tile.");
		} catch (NotEnoughNeedsException e) {
			this.popUp("Not enough needs", "Not enough needs...",
					"You must have enough needs to build this building.");
		} catch (BuildingAlreadyStartedException e) {
			this.popUp("Building already started", "Building already started...",
					"You must wait for the building to be finished.");
		} catch (NotBuiltException e) {
			this.popUp("Not built", "Not built...",
					"This building is not built yet.");
		} catch (NoBuildingHereException e1) {
			this.popUp("No building here", "No building here...",
					"There is no building here.");
		} catch (NotEnoughInhabitants e) {
			this.popUp("Not enough inhabitants", "Not enough inhabitants...",
					"You must have enough inhabitants to add them to this building.");
		} catch (NotEnoughWorkers e) {
			this.popUp("Not enough workers", "Not enough workers...",
					"You must have enough workers to add them to this building.");
		} catch (NoMoreSpace e) {
			this.popUp("No more space", "No more space...",
					"There is no more space to add people.");
		}
	}

	/***
	 * Creates a confirmation pop up with ok and cancel buttons
	 * 
	 * @param method  the method to run if the user clicks ok
	 * @param title
	 * @param header
	 * @param content
	 */
	private void confirmationPopUp(Runnable method, String title, String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				method.run();
			}
		});
	}

	private interface RunnableWithException {
		public void run() throws GameNotStarted, GameEnded, NotInGridException, NotFreeException,
				NotEnoughNeedsException, BuildingAlreadyStartedException, NotBuiltException, NoBuildingHereException,
				NotEnoughInhabitants, NotEnoughWorkers, NoMoreSpace;
	}

	private void createContextMenu() {
		MenuItem menuItemDestroy = new MenuItem("Destroy");
		MenuItem menuItemUpgrade = new MenuItem("Upgrade");
		MenuItem menuItemAddInhbitants = new MenuItem("Add Inhabitants");
		MenuItem menuItemRemoveInhabitants = new MenuItem("Remove Inhabitants");
		MenuItem menuItemAddWorkers = new MenuItem("Add Workers");
		MenuItem menuItemRemoveWorkers = new MenuItem("Remove Workers");

		menuItemDestroy.setOnAction(e -> {
			confirmationPopUp(() -> {
				makePopUpForException(() -> app.DestroyBuilding(x, y));
				updateAll();
			}, "Destroy building", "Destroy building...", "Are you sure you want to destroy this building?");
		});

		menuItemUpgrade.setOnAction(e -> {
			confirmationPopUp(() -> {
				makePopUpForException(() -> app.upgradeBuilding(x, y));
				updateAll();
			}, "Upgrade building", "Upgrade building...", "Are you sure you want to upgrade this building?");
		});

		menuItemAddInhbitants.setOnAction(e -> {
			InputPopup.openInputPopup(numberStr -> {
				Integer number;
				try {
					number = Integer.parseInt(numberStr);
				} catch (NumberFormatException exception) {
					this.popUp("Invalid number", "Invalid number...",
							"You must enter a valid number.");
					return;
				}
				makePopUpForException(() -> app.addInhabitant(x, y, number));
				updateAll();
			});
		});

		menuItemRemoveInhabitants.setOnAction(e -> {
			InputPopup.openInputPopup(numberStr -> {
				Integer number;
				try {
					number = Integer.parseInt(numberStr);
				} catch (NumberFormatException exception) {
					this.popUp("Invalid number", "Invalid number...",
							"You must enter a valid number.");
					return;
				}
				makePopUpForException(() -> app.removeInhabitant(x, y, number));
				updateAll();
			});
		});

		menuItemAddWorkers.setOnAction(e -> {
			InputPopup.openInputPopup(numberStr -> {
				Integer number;
				try {
					number = Integer.parseInt(numberStr);
				} catch (NumberFormatException exception) {
					this.popUp("Invalid number", "Invalid number...",
							"You must enter a valid number.");
					return;
				}
				makePopUpForException(() -> app.addWorker(x, y, number));
				updateAll();
			});
		});

		menuItemRemoveWorkers.setOnAction(e -> {
			InputPopup.openInputPopup(numberStr -> {
				Integer number;
				try {
					number = Integer.parseInt(numberStr);
				} catch (NumberFormatException exception) {
					this.popUp("Invalid number", "Invalid number...",
							"You must enter a valid number.");
					return;
				}
				makePopUpForException(() -> app.removeWorker(x, y, number));
				updateAll();
			});
		});

		contextMenu.getItems().addAll(menuItemDestroy, menuItemUpgrade, menuItemAddInhbitants,
				menuItemRemoveInhabitants, menuItemAddWorkers, menuItemRemoveWorkers);
	}

	private void updateAll() {
		updateables.forEach(updateable -> {
			updateable.update();
		});
	}

}