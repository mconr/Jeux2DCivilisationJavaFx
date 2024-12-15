package projet.approche.objet.domain.entities.building;

import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.domain.valueObject.building.Upgrade;
import projet.approche.objet.domain.valueObject.building.exceptions.BuildingAlreadyStartedException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotBuiltException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotEnoughNeedsException;
import projet.approche.objet.domain.valueObject.needs.ConstructionNeeds;
import projet.approche.objet.domain.valueObject.needs.Consumption;
import projet.approche.objet.domain.valueObject.needs.Production;
import projet.approche.objet.domain.valueObject.resource.ResourceList;

public class Building implements BuildingItf {

	public final long id;

	public final BuildingType type;
	private boolean buildStarted;
	private boolean isBuilt;
	private int time = 0; // time since last production / time since construction started
	private int inhabitants = 0; // current number of inhabitants in the building
	private int workers = 0; // current number of workers in the building
	private int level = 0; // current level of the building | levels are 0 (not built yet), 1, 2, 3, ...

	public int getInhabitants() {
		return inhabitants;
	}

	public int getWorkers() {
		return workers;
	}

	public int getLevel() {
		return level;
	}

	public Building(BuildingType buildingType, long id) {
		this.type = buildingType;
		this.id = id;
	}

	public boolean isBuildStarted() {
		return buildStarted;
	}

	public boolean isBuilt() {
		return isBuilt;
	}

	/**
	 * Updates the building. If the building is built, it will produce and consume
	 * resources if possible. If the building is not built but the construction
	 * started, it will continue the construction. If the building is not built and
	 * the construction did not start, nothing is done.
	 * 
	 * @param inventory the resources available to produce and consume
	 * @return the remaining resources after the production and consumption
	 */
	public ResourceList update(ResourceList inventory) {
		// reproduce inhabitants and workers at the begining of the "day"
		this.reproduce();
		ResourceList returnList = new ResourceList();
		if (isBuilt) { // if the building is built verify if it can produce
			if (this.workers >= this.getWorkersNeeded() && this.inhabitants >= this.getInhabitantsNeeded()) {
				time++;
				if (time >= this.getProduction().time && time >= this.getConsumption().time) { // even if it
					// should be
					// the same value
					if (this.getConsumption().isAffordable(inventory)) { // verify if the building have enough
						// resources
						returnList = this.getConsumption().getRemainingResources(inventory); // consume resources
						// from
						// inventory
						returnList = this.getProduction().harvestProduction(returnList); // produce resources in
						// inventory
						this.time = 0; // reset the time since last production
						return returnList;
					}
				}
			}
		} else if (buildStarted) { // if the building is not built but the construction started
			time++;
			if (time >= getConstructionNeeds().time) {
				isBuilt = true;
				this.level++;
				time = 0;
			}
		}
		// else the building is not built and the construction did not start so nothing
		// is done

		return inventory;
	}

	public Consumption getConsumption() {
		return this.type.getConsumption().multiply(Upgrade.UPGRADECONSUMPTION.getMultiplierByLevel(level));
	}

	public Production getProduction() {
		return this.type.getProduction().multiply(Upgrade.UPGRADEPRODUCTION.getMultiplierByLevel(level));
	}

	public int getWorkersNeeded() {
		return Math.round((this.type.getWorkersNeeded() * Upgrade.UPGRADEWORKERS.getMultiplierByLevel(level)));
	}

	public int getInhabitantsNeeded() {
		return Math.round((this.type.getInhabitantsNeeded() * Upgrade.UPGRADEINHABITANTS.getMultiplierByLevel(level)));
	}

	public int getWorkersMax() {
		return Math.round((this.type.getWorkersMax() * Upgrade.UPGRADEWORKERS.getMultiplierByLevel(level)));
	}

	public int getInhabitantsMax() {
		return Math.round((this.type.getInhabitantsMax() * Upgrade.UPGRADEINHABITANTS.getMultiplierByLevel(level)));
	}

	/**
	 * Starts the construction of the building.
	 * 
	 * @param resources the resources available to start the construction
	 * @return the remaining resources after the construction started
	 * @throws NotEnoughNeedsException         if the resources are not enough to
	 *                                         start the
	 *                                         construction
	 * @throws BuildingAlreadyStartedException if the building is already started
	 */
	public ResourceList startBuild(ResourceList resources)
			throws NotEnoughNeedsException, BuildingAlreadyStartedException {
		if (this.buildStarted)
			throw new BuildingAlreadyStartedException("building of " + this + " already started");
		if (this.getConstructionNeeds().isAffordable(resources)) {
			this.buildStarted = true;
			return this.getConstructionNeeds().getRemainingResources(resources);
		}
		throw new NotEnoughNeedsException("to build " + this);
	}

	public void addInhabitantToBuilding(int inhabitantsAdd) {
		this.inhabitants += inhabitantsAdd;
	}

	public void addWorkerToBuilding(int workersAdd) {
		this.workers += workersAdd;
	}

	public ConstructionNeeds getConstructionNeeds() {
		return this.type.getConstructionNeeds().multiply(Upgrade.UPGRADECOST.getMultiplierByLevel(level + 1));
	}

	public int getTimeToBuild() {
		return this.getConstructionNeeds().time;
	}

	public boolean canUpgrade(ResourceList inventory) {
		return this.getConstructionNeeds().isAffordable(inventory);
	}

	public ResourceList upgrade(ResourceList inventory)
			throws NotEnoughNeedsException, NotBuiltException, BuildingAlreadyStartedException {
		if (this.buildStarted && !this.isBuilt)
			throw new BuildingAlreadyStartedException("building of " + this + " already started");
		else if (!this.isBuilt)
			throw new NotBuiltException(this + " not built");
		if (this.canUpgrade(inventory)) {
			this.isBuilt = false;
			this.buildStarted = true;
			return this.getConstructionNeeds().getRemainingResources(inventory);
		}
		throw new NotEnoughNeedsException("to upgrade " + this);
	}

	public String toString() {
		if (isBuilt) {
			return type.name + " (level " + level + ") :\n\nNumber of inhabitants : " + inhabitants
					+ "\nNumber of workers : " + workers + "\n"
					+ getConsumption()
					+ getProduction();
		} else if (buildStarted) {
			return type.name + ":\nUnder construction";
		}
		return "";
	}

	public String toShortString() {
		return type.shortName + ":" + id;
	}

	/***
	 * Adds inhabitants and workers to the building. If the building is built, it
	 * will add inhabitants and workers to the building. Inhabitants and workers
	 * will reproduce only if the building is not less than half full.
	 */
	private void reproduce() {
		if (this.isBuilt) {
			int inhabitantsToAdd = this.inhabitants / 2;
			int workersToAdd = this.workers / 2;

			if (this.inhabitants >= this.getInhabitantsMax() / 2) {
				inhabitantsToAdd = 0;
			}
			if (this.workers >= this.getWorkersMax() / 2) {
				workersToAdd = 0;
			}

			this.inhabitants += inhabitantsToAdd;
			this.workers += workersToAdd;
		}
	}
}
