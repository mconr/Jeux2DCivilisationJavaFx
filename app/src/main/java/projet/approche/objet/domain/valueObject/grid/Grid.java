package projet.approche.objet.domain.valueObject.grid;

import java.util.Map;
import java.util.Collection;

import projet.approche.objet.domain.entities.building.Building;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotFreeException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;

public class Grid {

	public final int gridSize;
	public final Map<Coordinate, Building> buildings;

	public Grid(int gridSize) {
		this.gridSize = gridSize;
		this.buildings = new java.util.HashMap<>();
	}

	private Grid(int gridSize, Map<Coordinate, Building> buildings) {
		this.gridSize = gridSize;
		this.buildings = buildings;
	}

	public int getSize() {
		return this.gridSize;
	}

	public boolean isInGrid(Coordinate c) {
		return c.x >= 0 && c.x < gridSize && c.y >= 0 && c.y < gridSize;
	}

	public boolean isFree(Coordinate c) {
		return !buildings.containsKey(c);
	}

	public Grid setBuilding(Building b, Coordinate c) throws NotInGridException, NotFreeException {
		if (!isInGrid(c))
			throw new NotInGridException("The coordinate " + c + " is not in the grid");
		if (!isFree(c))
			throw new NotFreeException("The coordinate " + c + " is not free");
		Map<Coordinate, Building> toRet = new java.util.HashMap<>(buildings);
		toRet.put(c, b);
		return new Grid(gridSize, toRet);
	}

	public Building getBuilding(Coordinate c) throws NoBuildingHereException, NotInGridException {
		if (!isInGrid(c))
			throw new NotInGridException("The coordinate " + c + " is not in the grid");
		if (isFree(c))
			throw new NoBuildingHereException("There is no building at the coordinate " + c);
		return buildings.get(c);
	}

	public Grid removeBuilding(Coordinate c) throws NotInGridException, NoBuildingHereException {
		if (!isInGrid(c))
			throw new NotInGridException("The coordinate " + c + " is not in the grid");
		if (isFree(c))
			throw new NoBuildingHereException("There is no building at the coordinate " + c);
		Map<Coordinate, Building> toRet = new java.util.HashMap<>(buildings);
		toRet.remove(c);
		return new Grid(gridSize, toRet);
	}

	public Collection<Building> getBuildings() {
		return buildings.values();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < gridSize; i++) {
			sb.append("|");
			for (int j = 0; j < gridSize; j++) {
				Coordinate c = new Coordinate(i, j);
				if (buildings.containsKey(c)) {
					sb.append(buildings.get(c).type.shortName);
				} else {
					sb.append(" ");
				}
				sb.append("|");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
