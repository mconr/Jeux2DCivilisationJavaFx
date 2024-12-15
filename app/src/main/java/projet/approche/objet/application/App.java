package projet.approche.objet.application;

import java.util.ArrayList;
import java.util.List;

import projet.approche.objet.domain.aggregates.Manager;
import projet.approche.objet.domain.entities.building.Building;
import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.domain.valueObject.building.exceptions.BuildingAlreadyStartedException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotBuiltException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotEnoughNeedsException;
import projet.approche.objet.domain.valueObject.game.GameStarter;
import projet.approche.objet.domain.valueObject.game.GameState;
import projet.approche.objet.domain.valueObject.game.exceptions.GameAlreadyStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GameOverException;
import projet.approche.objet.domain.valueObject.game.exceptions.GamePaused;
import projet.approche.objet.domain.valueObject.game.exceptions.NoMoreSpace;
import projet.approche.objet.domain.valueObject.game.exceptions.NotEnoughInhabitants;
import projet.approche.objet.domain.valueObject.game.exceptions.NotEnoughWorkers;
import projet.approche.objet.domain.valueObject.grid.Coordinate;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotFreeException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;
import projet.approche.objet.domain.valueObject.resource.ResourceType;

public class App implements GameService, BuildingService, ResourceService {

	private Manager manager;

	public App(GameStarter gameStarter) {
		this.manager = new Manager(gameStarter);
	}

	@Override
	public List<String> getResourcesTypes() {
		List<String> resourcesTypes = new ArrayList<>();
		for (var resourceType : ResourceType.values()) {
			resourcesTypes.add(resourceType.toString());
		}
		return resourcesTypes;
	}

	/***
	 * Check if the game is started
	 * 
	 * @throws GameNotStarted if the game is not started
	 */
	private void checkGameStarted() throws GameNotStarted {
		if (!this.isGameStarted()) {
			throw new GameNotStarted();
		}
	}

	/***
	 * Check if the game is not started
	 * 
	 * @throws GameAlreadyStarted if the game is started
	 */
	private void checkGameNotStarted() throws GameAlreadyStarted {
		if (this.isGameStarted()) {
			throw new GameAlreadyStarted();
		}
	}

	/***
	 * Check if the game is ended
	 * 
	 * @throws GameEnded if the game is ended
	 */
	private void checkGameNotEnded() throws GameEnded {
		if (this.isGameEnded()) {
			throw new GameEnded();
		}
	}

	/***
	 * Check if the game is not paused
	 * 
	 * @throws GamePaused if the game is not paused
	 */
	private void checkGameNotPaused() throws GamePaused {
		if (this.isGamePaused()) {
			throw new GamePaused();
		}
	}

	public Manager getManager() {
		return this.manager;
	}

	@Override
	public int getResourceQuantity(String resourceType) {
		return this.manager.getResources().getAmount(ResourceType.valueOf(resourceType)).value;
	}

	@Override
	public int getResourceProduction(String resourceType) {
		return this.manager.getProduction(ResourceType.valueOf(resourceType));
	}

	@Override
	public int getInhabitantsNumber() {
		return this.manager.getAvailableInhabitants();
	}

	@Override
	public int getWorkersNumber() {
		return this.manager.getAvailableWorkers();
	}

	@Override
	public int getInHabitantsInBuildings() {
		return this.manager.getInhabitantsInBuildings();
	}

	@Override
	public int getWorkersInBuildings() {
		return this.manager.getWorkersInBuildings();
	}

	@Override
	public List<String> getBuildingsTypes() {
		List<String> buildingsTypes = new ArrayList<>();
		for (var buildingType : BuildingType.values()) {
			buildingsTypes.add(buildingType.toString());
		}
		return buildingsTypes;
	}

	@Override
	public String getBuildingType(int x, int y) throws NoBuildingHereException, NotInGridException {
		Building b = this.manager.getGrid().getBuilding(new Coordinate(x, y));
		if (!b.isBuilt())
			return "Construction";
		return b.type.toString();
	}

	@Override
	public String getBuildingStats(String buildingType) {
		return BuildingType.valueOf(buildingType).toString();
	}

	@Override
	public boolean isBuildingAffordable(String buildingType) {
		return this.manager.isBuildingAffordable(BuildingType.valueOf(buildingType));
	}

	@Override
	public void buildBuilding(String buildingType, int x, int y)
			throws GameNotStarted, GameEnded, NotInGridException, NotFreeException, NotEnoughNeedsException {
		checkGameStarted();
		checkGameNotEnded();
		this.manager.buildBuilding(BuildingType.valueOf(buildingType), new Coordinate(x, y));
	}

	@Override
	public void DestroyBuilding(int x, int y)
			throws GameNotStarted, GameEnded, NotInGridException, NoBuildingHereException {
		checkGameStarted();
		checkGameNotEnded();
		this.manager.destroyBuilding(new Coordinate(x, y));
	}

	@Override
	public void addInhabitant(int x, int y, int number) throws GameNotStarted, GameEnded, NotEnoughInhabitants,
			NotBuiltException, NoBuildingHereException, NotInGridException, NoMoreSpace {
		checkGameStarted();
		checkGameNotEnded();
		this.manager.addInhabitantToBuilding(this.manager.getGrid().getBuilding(new Coordinate(x, y)), number);
	}

	@Override
	public void removeInhabitant(int x, int y, int number) throws GameNotStarted, GameEnded, NotEnoughInhabitants,
			NotBuiltException, NoBuildingHereException, NotInGridException {
		checkGameStarted();
		checkGameNotEnded();
		this.manager.removeInhabitantFromBuilding(this.manager.getGrid().getBuilding(new Coordinate(x, y)), number);
	}

	@Override
	public void addWorker(int x, int y, int number) throws GameNotStarted, GameEnded, NotEnoughWorkers,
			NotBuiltException, NoMoreSpace, NoBuildingHereException, NotInGridException {
		checkGameStarted();
		checkGameNotEnded();
		this.manager.addWorkerToBuilding(this.manager.getGrid().getBuilding(new Coordinate(x, y)), number);
	}

	@Override
	public void removeWorker(int x, int y, int number) throws GameNotStarted, GameEnded, NotEnoughWorkers,
			NotBuiltException, NoBuildingHereException, NotInGridException {
		checkGameStarted();
		checkGameNotEnded();
		this.manager.removeWorkerFromBuilding(this.manager.getGrid().getBuilding(new Coordinate(x, y)), number);
	}

	@Override
	public void pauseGame() throws GameNotStarted, GameEnded {
		checkGameStarted();
		checkGameNotEnded();
		this.manager.pauseGame();
	}

	@Override
	public void startGame() throws GameEnded, GameAlreadyStarted {
		checkGameNotStarted();
		checkGameNotEnded();
		this.manager.startGame();
	}

	@Override
	public void endGame() throws GameNotStarted, GameEnded {
		checkGameStarted();
		checkGameNotEnded();
		this.manager.endGame();
	}

	@Override
	public void update() throws GameNotStarted, GameEnded, GamePaused, GameOverException {
		checkGameStarted();
		checkGameNotEnded();
		checkGameNotPaused();
		this.manager.update();
	}

	@Override
	public boolean isGameStarted() {
		return this.manager.getState().equals(GameState.RUNNING) || this.manager.getState().equals(GameState.PAUSED);
	}

	@Override
	public boolean isGameRunning() {
		return this.manager.getState().equals(GameState.RUNNING);
	}

	@Override
	public boolean isGameEnded() {
		return this.manager.getState().equals(GameState.ENDED);
	}

	@Override
	public boolean isGamePaused() {
		return this.manager.getState().equals(GameState.PAUSED);
	}

	@Override
	public int getGridSize() {
		return this.manager.getGrid().gridSize;
	}

	@Override
	public String getGameState() {
		return this.manager.getState().toString();
	}

	@Override
	public int getDay() {
		return this.manager.getDay();
	}

	@Override
	public boolean isBuildingUpgradeable(int x, int y) throws NoBuildingHereException, NotInGridException {
		return this.manager.isBuildingUpgradeable(this.manager.getGrid().getBuilding(new Coordinate(x, y)));
	}

	@Override
	public void upgradeBuilding(int x, int y) throws GameNotStarted, GameEnded, NotEnoughNeedsException,
			NotInGridException, NoBuildingHereException, NotBuiltException, BuildingAlreadyStartedException {
		checkGameStarted();
		checkGameNotEnded();
		this.manager.upgradeBuilding(this.manager.getGrid().getBuilding(new Coordinate(x, y)));
	}

	@Override
	public int getResourceConsumption(String resourceType) {
		return this.manager.getConsumption(ResourceType.valueOf(resourceType));
	}

	@Override
	public int getPureResourceProduction(String resourceType) {
		return this.manager.getPureProduction(ResourceType.valueOf(resourceType));
	}

	@Override
	public int getScore() {
		return this.manager.getScore();
	}
}
