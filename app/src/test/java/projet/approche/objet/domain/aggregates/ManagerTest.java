package projet.approche.objet.domain.aggregates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projet.approche.objet.domain.entities.building.Building;
import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.domain.valueObject.building.exceptions.NotBuiltException;
import projet.approche.objet.domain.valueObject.game.PremadeLevel;
import projet.approche.objet.domain.valueObject.game.GameStarter;
import projet.approche.objet.domain.valueObject.game.GameState;
import projet.approche.objet.domain.valueObject.game.exceptions.GameAlreadyStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.NoMoreSpace;
import projet.approche.objet.domain.valueObject.game.exceptions.NotEnoughInhabitants;
import projet.approche.objet.domain.valueObject.game.exceptions.NotEnoughWorkers;
import projet.approche.objet.domain.valueObject.grid.Coordinate;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotFreeException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;
import projet.approche.objet.domain.valueObject.resource.ResourceType;

import static org.junit.jupiter.api.Assertions.*;
import static projet.approche.objet.domain.valueObject.resource.ResourceType.FOOD;
import static projet.approche.objet.domain.valueObject.resource.ResourceType.GOLD;
import static projet.approche.objet.domain.valueObject.resource.ResourceType.STONE;
import static projet.approche.objet.domain.valueObject.resource.ResourceType.WOOD;

import java.util.List;

class ManagerTest {
	private Manager manager;
	private GameStarter gameStarter;
	private ResourceList inventory;

	@BeforeEach
	void setUp() {
		gameStarter = new GameStarter(PremadeLevel.EASY);
		manager = new Manager(gameStarter);
		assertDoesNotThrow(() -> manager.destroyBuilding(new Coordinate(0, 0)));
		assertDoesNotThrow(() -> manager.destroyBuilding(new Coordinate(9, 9)));
		assertDoesNotThrow(() -> manager.destroyBuilding(new Coordinate(9, 0)));
		assertDoesNotThrow(() -> manager.destroyBuilding(new Coordinate(0, 9)));
		assertDoesNotThrow(() -> manager.destroyBuilding(new Coordinate(4, 4)));
		assertDoesNotThrow(() -> manager.destroyBuilding(new Coordinate(4, 5)));
		assertDoesNotThrow(() -> manager.destroyBuilding(new Coordinate(5, 4)));
		assertDoesNotThrow(() -> manager.destroyBuilding(new Coordinate(5, 5)));
		manager.setAvailableInhabitants(1000);
		manager.setAvailableWorkers(1000);
		this.inventory = new ResourceList(
				List.of(new Resource(ResourceType.GOLD, 100), new Resource(ResourceType.WOOD, 100),
						new Resource(ResourceType.STONE, 100), new Resource(ResourceType.FOOD, 100)));

	}

	@Test
	void testBuildBuilding() {
		BuildingType building = BuildingType.WOODENCABIN;
		Coordinate coordinate = new Coordinate(0, 0);
		assertDoesNotThrow(() -> manager.buildBuilding(building, coordinate));
		assertDoesNotThrow(() -> manager.getGrid().getBuilding(coordinate));
	}

	@Test
	void testBuildBuildingNotInGrid() {
		BuildingType building = BuildingType.WOODENCABIN;
		Coordinate coordinate = new Coordinate(10, 10);
		assertThrows(NotInGridException.class, () -> manager.buildBuilding(building, coordinate));
		assertThrows(NotInGridException.class, () -> manager.getGrid().getBuilding(coordinate));
	}

	@Test
	void testBuildBuildingNotFree() {
		BuildingType building = BuildingType.WOODENCABIN;
		Coordinate coordinate = new Coordinate(0, 0);
		assertDoesNotThrow(() -> manager.buildBuilding(building, coordinate));
		assertThrows(NotFreeException.class, () -> manager.buildBuilding(building, coordinate));
		assertDoesNotThrow(() -> manager.getGrid().getBuilding(coordinate));
	}

	@Test
	void testDestroyBuilding() {
		BuildingType building = BuildingType.WOODENCABIN;
		Coordinate coordinate = new Coordinate(0, 0);
		assertDoesNotThrow(() -> manager.buildBuilding(building, coordinate));
		assertDoesNotThrow(() -> manager.destroyBuilding(coordinate));
		assertThrows(NoBuildingHereException.class, () -> manager.getGrid().getBuilding(coordinate));
	}

	@Test
	void testDestroyBuildingNotInGrid() {
		Coordinate coordinate = new Coordinate(10, 10);
		assertThrows(NotInGridException.class, () -> manager.destroyBuilding(coordinate));
	}

	@Test
	void testDestroyBuildingNoBuildingHere() {
		Coordinate coordinate = new Coordinate(0, 0);
		assertThrows(NoBuildingHereException.class, () -> manager.destroyBuilding(coordinate));
	}

	@Test
	void testAddInhabitantToBuilding() throws NoBuildingHereException, NotInGridException {
		BuildingType building = BuildingType.WOODENCABIN;
		Coordinate coordinate = new Coordinate(0, 0);
		assertDoesNotThrow(() -> manager.buildBuilding(building, coordinate));
		Building b = manager.getGrid().getBuilding(coordinate);
		assertThrows(NotBuiltException.class, () -> manager.addInhabitantToBuilding(b, 5));
		while (!b.isBuilt())
			b.update(inventory);
		assertDoesNotThrow(() -> manager.addInhabitantToBuilding(b, 4));
		assertDoesNotThrow(() -> manager.addInhabitantToBuilding(b, 4));
		assertThrows(NoMoreSpace.class, () -> manager.addInhabitantToBuilding(b, 2));
		assertEquals(8, b.getInhabitants());
	}

	@Test
	void testRemoveInhabitantFromBuilding() throws NoBuildingHereException, NotInGridException {
		BuildingType building = BuildingType.WOODENCABIN;
		Coordinate coordinate = new Coordinate(0, 0);
		assertDoesNotThrow(() -> manager.buildBuilding(building, coordinate));
		Building b = manager.getGrid().getBuilding(coordinate);
		while (!b.isBuilt())
			b.update(inventory);
		assertThrows(NotEnoughInhabitants.class, () -> manager.removeInhabitantFromBuilding(b, 5));
		assertDoesNotThrow(() -> manager.addInhabitantToBuilding(b, 4));
		assertDoesNotThrow(() -> manager.removeInhabitantFromBuilding(b, 3));
		assertEquals(1, b.getInhabitants());
	}

	@Test
	void testAddWorkerToBuilding() throws NoBuildingHereException, NotInGridException {
		BuildingType building = BuildingType.WOODENCABIN;
		Coordinate coordinate = new Coordinate(0, 0);
		assertDoesNotThrow(() -> manager.buildBuilding(building, coordinate));
		Building b = manager.getGrid().getBuilding(coordinate);
		assertThrows(NotBuiltException.class, () -> manager.addWorkerToBuilding(b, 5));
		while (!b.isBuilt())
			b.update(inventory);
		assertDoesNotThrow(() -> manager.addWorkerToBuilding(b, 8));
		assertThrows(NoMoreSpace.class, () -> manager.addWorkerToBuilding(b, 2));
		assertEquals(8, b.getWorkers());
	}

	@Test
	void testRemoveWorkerFromBuilding() throws NoBuildingHereException, NotInGridException {
		BuildingType building = BuildingType.WOODENCABIN;
		Coordinate coordinate = new Coordinate(0, 0);
		assertDoesNotThrow(() -> manager.buildBuilding(building, coordinate));
		Building b = manager.getGrid().getBuilding(coordinate);
		while (!b.isBuilt())
			b.update(inventory);
		assertThrows(NotEnoughWorkers.class, () -> manager.removeWorkerFromBuilding(b, 5));
		assertDoesNotThrow(() -> manager.addWorkerToBuilding(b, 4));
		assertDoesNotThrow(() -> manager.removeWorkerFromBuilding(b, 3));
		assertEquals(1, b.getWorkers());
	}

	@Test
	void testStartGame() {
		try {
			manager.startGame();
		} catch (GameAlreadyStarted | GameEnded e) {
			assertFalse(true); // SHOULD NOT HAPPEN
		}
		assertEquals(GameState.RUNNING, manager.getState());
	}

	@Test
	void testPauseGame() {
		try {
			manager.startGame();
		} catch (GameAlreadyStarted | GameEnded e) {
			assertFalse(true); // SHOULD NOT HAPPEN
		}
		try {
			manager.pauseGame();
		} catch (GameNotStarted | GameEnded e) {
			assertFalse(true); // SHOULD NOT HAPPEN
		}
		assertEquals(GameState.PAUSED, manager.getState());
	}

	@Test
	void testEndGame() {
		try {
			manager.startGame();
		} catch (GameAlreadyStarted | GameEnded e) {
			assertFalse(true); // SHOULD NOT HAPPEN
		}
		try {
			manager.endGame();
		} catch (GameNotStarted e) {
			assertFalse(true); // SHOULD NOT HAPPEN
		}
		assertEquals(GameState.ENDED, manager.getState());
	}

	@Test
	void testGetProduction() throws NoBuildingHereException, NotInGridException {
		BuildingType building = BuildingType.WOODENCABIN;
		Coordinate coordinate = new Coordinate(0, 0);
		assertDoesNotThrow(() -> manager.buildBuilding(building, coordinate));
		Building b = manager.getGrid().getBuilding(coordinate);
		assertEquals(0, manager.getProduction(WOOD));
		assertEquals(0, manager.getProduction(FOOD));
		assertEquals(0, manager.getProduction(GOLD));
		assertEquals(0, manager.getProduction(STONE));
		assertThrows(NotBuiltException.class, () -> manager.addWorkerToBuilding(b, 4));
		assertThrows(NotBuiltException.class, () -> manager.addInhabitantToBuilding(b, 4));
		while (!b.isBuilt())
			b.update(inventory);
		assertEquals(0, manager.getProduction(WOOD));
		assertEquals(0, manager.getProduction(FOOD));
		assertEquals(0, manager.getProduction(GOLD));
		assertEquals(0, manager.getProduction(STONE));
		assertDoesNotThrow(() -> manager.addWorkerToBuilding(b, 8));
		assertEquals(0, manager.getProduction(WOOD));
		assertEquals(0, manager.getProduction(FOOD));
		assertEquals(0, manager.getProduction(GOLD));
		assertEquals(0, manager.getProduction(STONE));
		assertDoesNotThrow(() -> manager.addInhabitantToBuilding(b, 8));
		assertEquals(6, manager.getProduction(WOOD));
		assertEquals(8, manager.getProduction(FOOD));
		assertEquals(0, manager.getProduction(GOLD));
		assertEquals(0, manager.getProduction(STONE));
		BuildingType building2 = BuildingType.FARM;
		Coordinate coordinate2 = new Coordinate(1, 1);
		manager.setResources(inventory);
		assertDoesNotThrow(() -> manager.buildBuilding(building2, coordinate2));
		Building b2 = manager.getGrid().getBuilding(coordinate2);
		while (!b2.isBuilt())
			b2.update(inventory);
		manager.setAvailableWorkers(100);
		manager.setAvailableInhabitants(100);
		assertDoesNotThrow(() -> manager.addWorkerToBuilding(b2, 4));
		assertDoesNotThrow(() -> manager.addInhabitantToBuilding(b2, 6));
		assertEquals(6, manager.getProduction(WOOD));
		assertEquals(8, manager.getProduction(FOOD));
		assertEquals(0, manager.getProduction(GOLD));
		assertEquals(0, manager.getProduction(STONE));
	}
	// TODO: update() test
}