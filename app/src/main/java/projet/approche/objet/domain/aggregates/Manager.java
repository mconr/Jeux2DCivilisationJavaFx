package projet.approche.objet.domain.aggregates;

import java.util.Collection;

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
import projet.approche.objet.domain.valueObject.grid.Grid;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotFreeException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceAmount;
import projet.approche.objet.domain.valueObject.resource.ResourceList;
import projet.approche.objet.domain.valueObject.resource.ResourceType;
import projet.approche.objet.domain.valueObject.resource.exceptions.NotEnoughResourceException;
import projet.approche.objet.domain.valueObject.resource.exceptions.ResourceNotFoundException;

public class Manager {
	private static Long count = Long.valueOf(0);
	private static Long idBuildings = Long.valueOf(0);

	private final Long id = ++count;

	private int availableInhabitants; // number of inhabitants
	private int availableWorkers; // number of workers

	private int day = 0; // number of days since the beginning of the game

	private GameState state = GameState.NOTSTARTED;

	private Grid grid; // buildings are in the grid
	private ResourceList resources = new ResourceList();

	private int score = 0;

	/**
	 * Manager of the game, it will manage the buildings, the resources, the
	 * inhabitants and the workers
	 * 
	 * @param gameStarter the game starter
	 */
	public Manager(GameStarter gameStarter) {
		this.availableInhabitants = gameStarter.inhabitants;
		this.availableWorkers = gameStarter.workers;
		this.resources = gameStarter.startingResources;
		this.grid = new Grid(gameStarter.gridSize);
		for (var coordinate : gameStarter.startingBuildings.keySet()) {
			Building starterBuilding = new Building(gameStarter.startingBuildings.get(coordinate),
					++idBuildings);
			try {
				starterBuilding.startBuild(getInfiniteResources());
			} catch (NotEnoughNeedsException | BuildingAlreadyStartedException e) {
				// Shouldn't happen since it's a gameStarter otherwise throw an exception
				throw new RuntimeException(e);
			}
			while (!starterBuilding.isBuilt()) {
				starterBuilding.update(getInfiniteResources());
			}
			starterBuilding.addInhabitantToBuilding(starterBuilding.type.getInhabitantsMax() / 2);
			starterBuilding.addWorkerToBuilding(starterBuilding.type.getWorkersMax() / 2);
			try {
				this.grid = this.grid.setBuilding(starterBuilding, coordinate);

			} catch (NotInGridException | NotFreeException e) {
				// Shouldn't happen since it's a gameStarter otherwise throw an exception
				throw new RuntimeException(e);
			}
		}
	}

	private ResourceList getInfiniteResources() {
		ResourceList infiniteInv = new ResourceList();
		for (var resourceType : ResourceType.values()) {
			infiniteInv = infiniteInv.add(new Resource(resourceType, new ResourceAmount(1000000)));
		}
		return infiniteInv;
	}

	public Long getId() {
		return id;
	}

	public int getAvailableInhabitants(){
		return availableInhabitants;
	}

	public void setAvailableInhabitants(int inhabitants) {
		this.availableInhabitants = inhabitants;
	}

	public int getAvailableWorkers() {
		return availableWorkers;
	}

	public void setAvailableWorkers(int workers) {
		this.availableWorkers = workers;
	}

	public GameState getState() {
		return state;
	}

	public ResourceList getResources() {
		return resources;
	}

	public boolean isBuildingAffordable(BuildingType buildingType) {
		return buildingType.getConstructionNeeds().isAffordable(resources);
	}

	/**
	 * @brief Build a building on the grid at the given coordinate with the given
	 *        type of building and start the building process of the building
	 * @param buildingType the type of building to build
	 * @param c            the coordinate where to build the building
	 * @throws NotInGridException
	 * @throws NotFreeException
	 * @throws NotEnoughNeedsException
	 */
	public void buildBuilding(BuildingType buildingType, Coordinate c) throws NotInGridException, NotFreeException,
			NotEnoughNeedsException {
		if (!this.grid.isFree(c))
			throw new NotFreeException("The coordinate " + c + " is not free");
		Building b = new Building(buildingType, ++idBuildings);
		try {
			this.startBuildBuilding(b);
		} catch (NoBuildingHereException | BuildingAlreadyStartedException e) {
			throw new RuntimeException(e); // cannot happen since we just created the building
		}
		this.grid = this.grid.setBuilding(b, c);
	}

	public void destroyBuilding(Coordinate c) throws NotInGridException, NoBuildingHereException {
		// refund half the resources it took to build the building (ignoring upgrades)
		ResourceList buildingBuildResources = this.grid.getBuilding(c).type.getConstructionNeeds()
				.multiply(0.5f).resources;
		this.resources = this.resources.add(buildingBuildResources);
		this.grid = this.grid.removeBuilding(c);
	}

	private void startBuildBuilding(Building b) throws NotInGridException, NoBuildingHereException,
			NotEnoughNeedsException, BuildingAlreadyStartedException {
		resources = b.startBuild(resources);
	}

	public Grid getGrid() {
		return grid;
	}

	public void addInhabitantToBuilding(Building building, int inhabitantsToAdd)
			throws NotEnoughInhabitants, NotBuiltException, NoMoreSpace {
		if (!building.isBuilt())
			throw new NotBuiltException();
		if (building.getInhabitants() + inhabitantsToAdd <= building.type.getInhabitantsMax()) {
			if (this.availableInhabitants >= inhabitantsToAdd) {
				building.addInhabitantToBuilding(inhabitantsToAdd);
				this.availableInhabitants -= inhabitantsToAdd;
			} else {
				throw new NotEnoughInhabitants();
			}
		} else {
			throw new NoMoreSpace();
		}
	}

	public void removeInhabitantFromBuilding(Building building, int inhabitantsToRemove)
			throws NotEnoughInhabitants, NotBuiltException {
		if (!building.isBuilt())
			throw new NotBuiltException();
		if (building.getInhabitants() >= inhabitantsToRemove) {
			building.addInhabitantToBuilding(-inhabitantsToRemove);
			this.availableInhabitants += inhabitantsToRemove;
		} else {
			throw new NotEnoughInhabitants();
		}
	}

	public void addWorkerToBuilding(Building building, int workersToAdd)
			throws NotEnoughWorkers, NotBuiltException, NoMoreSpace {
		if (!building.isBuilt())
			throw new NotBuiltException();
		if (building.getWorkers() + workersToAdd <= building.type.getWorkersMax()) {
			if (this.availableWorkers >= workersToAdd) {
				building.addWorkerToBuilding(workersToAdd);
				this.availableWorkers -= workersToAdd;
			} else {
				throw new NotEnoughWorkers();
			}
		} else {
			throw new NoMoreSpace();
		}
	}

	public void removeWorkerFromBuilding(Building building, int workersToRemove)
			throws NotEnoughWorkers, NotBuiltException {
		if (!building.isBuilt())
			throw new NotBuiltException();
		if (building.getWorkers() >= workersToRemove) {
			building.addWorkerToBuilding(-workersToRemove);
			this.availableWorkers += workersToRemove;
		} else {
			throw new NotEnoughWorkers();
		}
	}

	public void startGame() throws GameAlreadyStarted, GameEnded {
		if (state == GameState.NOTSTARTED)
			state = GameState.RUNNING;
		else if (state == GameState.ENDED)
			throw new GameEnded();
		else if (state == GameState.RUNNING || state == GameState.PAUSED)
			throw new GameAlreadyStarted();
	}

	public void pauseGame() throws GameNotStarted, GameEnded {
		if (state == GameState.RUNNING) {
			state = GameState.PAUSED;
		} else if (state == GameState.PAUSED) {
			state = GameState.RUNNING;
		} else if (state == GameState.NOTSTARTED) {
			throw new GameNotStarted();
		} else if (state == GameState.ENDED) {
			throw new GameEnded();
		}
	}

	public void endGame() throws GameNotStarted {
		if (state == GameState.RUNNING || state == GameState.PAUSED) {
			state = GameState.ENDED;
		} else if (state == GameState.NOTSTARTED) {
			throw new GameNotStarted();
		}
	}

	/**
	 * @throws GameOverException
	 * @breif Update the game and all its components
	 * @details Update the game and all its components, it will update the
	 *          buildings, the resources, the inhabitants and the workers (kill all
	 *          of them who are not in a building)
	 */
	public void update() throws GameNotStarted, GameEnded, GamePaused, GameOverException {
		if (state == GameState.NOTSTARTED) {
			throw new GameNotStarted();
		} else if (state == GameState.ENDED) {
			throw new GameEnded();
		} else if (state == GameState.PAUSED) {
			throw new GamePaused();
		} else if (state == GameState.RUNNING) {
			// update the day
			this.day++;
			Collection<Building> buildings = this.grid.getBuildings();
			for (Building building : buildings) {
				resources = building.update(resources);
			}
			// get food needed for the next day
			int foodNeededInt = this.foodConsumption();
			Resource foodNeeded = new Resource(ResourceType.FOOD, new ResourceAmount(foodNeededInt));
			// remove food needed for the next day
			try {
				resources = resources.remove(foodNeeded);
			} catch (NotEnoughResourceException | ResourceNotFoundException e) {
				this.endGame();
				throw new GameOverException("You don't have enough food to feed your people ...");
			}
			if ((this.getInhabitantsInBuildings() == 0 && this.availableInhabitants == 0)
					|| (this.getWorkersInBuildings() == 0 && this.availableWorkers == 0)) {
				this.endGame();
				throw new GameOverException("You don't have any inhabitants/workers left ...");
			}
		}
		// kill inhabitants and workers who are not in a building
		this.availableInhabitants = 0;
		this.availableWorkers = 0;
		this.computeScore();
	}

	/***
	 * @brief Get the production of a resource for each update
	 * @param resourceType the resource that we want to know the production
	 * @return the production of the resource for each update
	 */
	public int getProduction(ResourceType resourceType) {
		int production = 0;
		for (Building building : this.grid.getBuildings()) {
			if (building.type.getInhabitantsNeeded() <= building.getInhabitants()
					&& building.type.getWorkersNeeded() <= building.getWorkers())
				for (Resource r : building.type.getProduction().resources)
					if (r.type == resourceType)
						production += r.amount.value / building.type.getProduction().time;
		}
		return production;
	}

	public int getConsumption(ResourceType resourceType) {
		if (resourceType == ResourceType.FOOD)
			return this.foodConsumption();
		int consumption = 0;
		for (Building building : this.grid.getBuildings()) {
			if (building.type.getInhabitantsNeeded() <= building.getInhabitants()
					&& building.type.getWorkersNeeded() <= building.getWorkers())
				for (Resource r : building.type.getConsumption().resources)
					if (r.type == resourceType)
						consumption += r.amount.value / building.type.getConsumption().time;
		}
		return consumption;
	}

	public int getPureProduction(ResourceType resourceType) {
		return this.getProduction(resourceType) - this.getConsumption(resourceType);
	}

	public int foodConsumption() {
		// minimum amount of food needed for the next day
		int foodNeeded = 0;
		// add food needed for each building by adding its inhabitants and workers
		for (Building building : this.grid.getBuildings()) {
			foodNeeded += building.getInhabitants() + building.getWorkers();
		}
		return foodNeeded;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(GameState state) {
		this.state = state;
	}

	/**
	 * @param buildings the grid to set
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(ResourceList resources) {
		this.resources = resources;
	}

	public int getInhabitantsInBuildings() {
		int cpt = 0;
		for (Building building : this.grid.getBuildings()) {
			cpt += building.getInhabitants();
		}
		return cpt;
	}

	public int getWorkersInBuildings() {
		int cpt = 0;
		for (Building building : this.grid.getBuildings()) {
			cpt += building.getWorkers();
		}
		return cpt;
	}

	public int getDay() {
		return day;
	}

	public int setDay() {
		return day;
	}

	public boolean isBuildingUpgradeable(Building building) {
		return building.canUpgrade(this.resources);
	}

	public void upgradeBuilding(Building building)
			throws NotEnoughNeedsException, NotBuiltException, BuildingAlreadyStartedException {
		this.resources = building.upgrade(this.resources);
	}

	public int getScore() {
		return score;
	}

	private void computeScore() {
		score = 0;
		for (Resource resource : resources) {
			score += Math.round(resource.amount.value * resource.type.valueMultiplier);
		}
	}
}
