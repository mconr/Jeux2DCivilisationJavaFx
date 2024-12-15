package projet.approche.objet.application;

import java.util.List;

public interface ResourceService {

	public List<String> getResourcesTypes();

	public int getResourceQuantity(String resourceType);

	public int getResourceProduction(String resourceType);

	public int getResourceConsumption(String resourceType);

	public int getPureResourceProduction(String resourceType);

	public int getInhabitantsNumber();

	public int getWorkersNumber();

	public int getInHabitantsInBuildings();

	public int getWorkersInBuildings();
}
