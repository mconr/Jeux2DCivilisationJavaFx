// FILEPATH: /home/nessar/Projet-Approche-Objet/app/src/test/java/projet/approche/objet/domain/entities/building/BuildingTest.java

package projet.approche.objet.domain.entities.building;

import org.junit.jupiter.api.Test;

import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.domain.valueObject.building.exceptions.BuildingAlreadyStartedException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotBuiltException;
import projet.approche.objet.domain.valueObject.building.exceptions.NotEnoughNeedsException;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;
import projet.approche.objet.domain.valueObject.resource.ResourceType;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BuildingTest {
	@Test
	void testConstructor() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);

		assertEquals(type, building.type);
		assertFalse(building.isBuildStarted());
		assertFalse(building.isBuilt());
	}

	@Test
	void testStartBuild() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);
		ResourceList resources = new ResourceList(List.of(new Resource(ResourceType.WOOD, 100)));
		assertThrows(NotEnoughNeedsException.class, () -> building.startBuild(resources));
		ResourceList resources2 = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 100),
				new Resource(ResourceType.STONE, 100)));
		assertThrows(NotEnoughNeedsException.class, () -> building.startBuild(resources2));
		ResourceList resources3 = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 100),
				new Resource(ResourceType.STONE, 100),
				new Resource(ResourceType.GOLD, 100)));
		assertDoesNotThrow(() -> building.startBuild(resources3));
		assertTrue(building.isBuildStarted());
	}

	@Test
	void testStartBuildAlreadyStarted() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);

		ResourceList resources = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 100),
				new Resource(ResourceType.STONE, 100),
				new Resource(ResourceType.GOLD, 100)));

		assertDoesNotThrow(() -> building.startBuild(resources));
		assertThrows(BuildingAlreadyStartedException.class, () -> building.startBuild(resources));
	}

	@Test
	void testUpdateBuilding() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);

		ResourceList resources = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 100),
				new Resource(ResourceType.STONE, 100),
				new Resource(ResourceType.GOLD, 100)));

		assertDoesNotThrow(() -> building.startBuild(resources)); // start building
		for (int i = 0; i < type.getConstructionNeeds().time; i++) {
			assertDoesNotThrow(() -> building.update(resources));
		}
		assertTrue(building.isBuilt()); // building is built
	}

	@Test
	void testUpdateBuildingProduction() {
		BuildingType type = BuildingType.LUMBERMILL;
		Building building = new Building(type, 1);

		ResourceList resourcesForBuild = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 100),
				new Resource(ResourceType.STONE, 100),
				new Resource(ResourceType.GOLD, 100)));

		assertDoesNotThrow(() -> building.startBuild(resourcesForBuild));
		for (int i = 0; i < type.getConstructionNeeds().time; i++) {
			assertDoesNotThrow(() -> building.update(resourcesForBuild));
		}
		assertTrue(building.isBuilt()); // building is built

		ResourceList resources2 = new ResourceList(List.of(new Resource(ResourceType.WOOD, 5)));
		ResourceList resources3 = building.update(resources2);
		assertEquals(resources2, resources3); // no production as there is no worker / inhabitant

		building.addInhabitantToBuilding(100);
		resources3 = building.update(resources2);
		assertEquals(resources2, resources3); // no production as there is no worker

		building.addWorkerToBuilding(100);
		resources3 = building.update(resources2);
		ResourceList shouldBe = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 1),
				new Resource(ResourceType.LUMBER, 4)));
		assertNotEquals(resources2, resources3); // production
		assertTrue(resources3.contains(shouldBe)); // production and verification of the remaining resources

		ResourceList resources4 = new ResourceList(List.of(new Resource(ResourceType.WOOD, 3)));
		ResourceList resources5 = building.update(resources4);
		assertEquals(resources4, resources5); // no production as there is not enough resources
	}

	@Test
	void testAddInhabitantToBuilding() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);

		building.addInhabitantToBuilding(5);
		assertEquals(5, building.getInhabitants());
	}

	@Test
	void testRemoveInhabitantFromBuilding() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);

		building.addInhabitantToBuilding(5);
		building.addInhabitantToBuilding(-2);
		assertEquals(3, building.getInhabitants());
	}

	@Test
	void testAddWorkerToBuilding() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);

		building.addWorkerToBuilding(5);
		assertEquals(5, building.getWorkers());
	}

	@Test
	void testRemoveWorkerFromBuilding() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);

		building.addWorkerToBuilding(5);
		building.addWorkerToBuilding(-2);
		assertEquals(3, building.getWorkers());
	}

	@Test
	void testCanUpgrade() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);

		ResourceList inventory = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 50),
				new Resource(ResourceType.STONE, 50),
				new Resource(ResourceType.GOLD, 50)));

		ResourceList emptyInventory = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 0),
				new Resource(ResourceType.STONE, 0),
				new Resource(ResourceType.GOLD, 0)));

		assertFalse(building.canUpgrade(emptyInventory));

		assertDoesNotThrow(() -> building.startBuild(inventory));

		while (!building.isBuilt())
			building.update(inventory);

		assertTrue(building.canUpgrade(inventory));
	}

	@Test
	void testUpgrade() {
		BuildingType type = BuildingType.HOUSE;
		Building building = new Building(type, 1);

		ResourceList inventory = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 50),
				new Resource(ResourceType.STONE, 50),
				new Resource(ResourceType.GOLD, 50)));

		ResourceList emptyInventory = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 0),
				new Resource(ResourceType.STONE, 0),
				new Resource(ResourceType.GOLD, 0)));

		assertThrows(NotBuiltException.class, () -> building.upgrade(inventory));

		assertDoesNotThrow(() -> building.startBuild(inventory));

		while (!building.isBuilt())
			building.update(inventory);

		assertThrows(NotEnoughNeedsException.class, () -> building.upgrade(emptyInventory));

		ResourceList remainingResources;
		try {
			remainingResources = building.upgrade(inventory);
		} catch (NotEnoughNeedsException | NotBuiltException | BuildingAlreadyStartedException e) {
			assertTrue(false);
			return;
		}

		ResourceList exceptedRemainingResources = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 48),
				new Resource(ResourceType.STONE, 48),
				new Resource(ResourceType.GOLD, 49)));

		assertEquals(exceptedRemainingResources, remainingResources);
		assertEquals(1, building.getLevel());
		assertFalse(building.isBuilt());
		assertTrue(building.isBuildStarted());

		while (!building.isBuilt())
			building.update(inventory);

		assertEquals(2, building.getLevel());
		assertTrue(building.isBuilt());

		var apartmentT = BuildingType.APARTMENTBUILDING;
		var appartment = new Building(apartmentT, 2);

		var inventory2 = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 100),
				new Resource(ResourceType.STONE, 100),
				new Resource(ResourceType.GOLD, 100)));

		assertDoesNotThrow(() -> appartment.startBuild(inventory2));

		while (!appartment.isBuilt())
			appartment.update(inventory2);

		assertThrows(NotEnoughNeedsException.class, () -> appartment.upgrade(emptyInventory));

		ResourceList remainingResources2;
		try {
			remainingResources2 = appartment.upgrade(inventory2);
		} catch (NotEnoughNeedsException | NotBuiltException | BuildingAlreadyStartedException e) {
			assertTrue(false);
			return;
		}

		ResourceList exceptedRemainingResources2 = new ResourceList(List.of(
				new Resource(ResourceType.WOOD, 62),
				new Resource(ResourceType.STONE, 62),
				new Resource(ResourceType.GOLD, 97)));

		assertEquals(exceptedRemainingResources2, remainingResources2);
		assertEquals(1, appartment.getLevel());
		assertFalse(appartment.isBuilt());
		assertTrue(appartment.isBuildStarted());

		while (!appartment.isBuilt())
			appartment.update(inventory);

		assertEquals(2, appartment.getLevel());
		assertTrue(appartment.isBuilt());
	}
}