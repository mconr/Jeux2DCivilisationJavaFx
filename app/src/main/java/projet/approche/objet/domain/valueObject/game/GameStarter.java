package projet.approche.objet.domain.valueObject.game;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.domain.valueObject.grid.Coordinate;
import projet.approche.objet.domain.valueObject.resource.ResourceList;

public class GameStarter {

	public final int gridSize;
	public final int inhabitants;
	public final int workers;
	public final ResourceList startingResources;
	public final Map<Coordinate, BuildingType> startingBuildings;
	public final String name;

	public GameStarter(PremadeLevel level) {
		name = level.name();
		this.gridSize = level.gridSize;
		this.inhabitants = level.inhabitants;
		this.workers = level.workers;
		this.startingResources = level.startingResources;
		this.startingBuildings = level.startingBuildings;
	}

	public GameStarter(int gridSize, int inhabitants, int workers, ResourceList startingResources,
			List<SimpleEntry<Coordinate, BuildingType>> entries) {
		name = "Saved game";
		this.gridSize = gridSize;
		this.inhabitants = inhabitants;
		this.workers = workers;
		this.startingResources = startingResources;
		var tmp = new HashMap<Coordinate, BuildingType>();
		for (Entry<Coordinate, BuildingType> entry : entries) {
			tmp.put(entry.getKey(), entry.getValue());
		}
		this.startingBuildings = tmp;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
