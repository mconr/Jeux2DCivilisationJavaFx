package projet.approche.objet.application;

import java.util.List;

import projet.approche.objet.domain.valueObject.building.exceptions.BuildingAlreadyStartedException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotBuiltException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotEnoughNeedsException;
import projet.approche.objet.domain.valueObject.game.exceptions.GameEnded;
import projet.approche.objet.domain.valueObject.game.exceptions.GameNotStarted;
import projet.approche.objet.domain.valueObject.game.exceptions.NoMoreSpace;
import projet.approche.objet.domain.valueObject.game.exceptions.NotEnoughInhabitants;
import projet.approche.objet.domain.valueObject.game.exceptions.NotEnoughWorkers;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotFreeException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;

public interface BuildingService {

	public List<String> getBuildingsTypes();

	public String getBuildingType(int x, int y) throws NoBuildingHereException, NotInGridException;

	public String getBuildingStats(String buildingType);

	public void buildBuilding(String buildingType, int x, int y)
			throws GameNotStarted, GameEnded, NotEnoughNeedsException, NotInGridException, NotFreeException;

	public void DestroyBuilding(int x, int y)
			throws GameNotStarted, GameEnded, NotInGridException, NoBuildingHereException;

	public void addInhabitant(int x, int y, int number)
			throws GameNotStarted, GameEnded, NotEnoughInhabitants, NotInGridException, NotBuiltException,
			NoBuildingHereException, NoMoreSpace;

	public void removeInhabitant(int x, int y, int number)
			throws GameNotStarted, GameEnded, NotEnoughInhabitants, NotInGridException, NotBuiltException,
			NoBuildingHereException;

	public void addWorker(int x, int y, int number)
			throws GameNotStarted, GameEnded, NotEnoughWorkers, NotInGridException, NotBuiltException, NoMoreSpace,
			NoBuildingHereException;

	public void removeWorker(int x, int y, int number)
			throws GameNotStarted, GameEnded, NotEnoughWorkers, NotInGridException, NotBuiltException,
			NoBuildingHereException;

	public boolean isBuildingAffordable(String buildingType);

	public boolean isBuildingUpgradeable(int x, int y) throws NoBuildingHereException, NotInGridException;

	public void upgradeBuilding(int x, int y)
			throws GameNotStarted, GameEnded, NotEnoughNeedsException, NotInGridException, NoBuildingHereException,
			NotBuiltException, BuildingAlreadyStartedException;
}
