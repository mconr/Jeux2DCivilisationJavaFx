package projet.approche.objet.domain.valueObject.needs;

import org.junit.jupiter.api.Test;

import projet.approche.objet.domain.valueObject.needs.exceptions.NotEnoughTimeException;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;
import projet.approche.objet.domain.valueObject.resource.ResourceType;
import projet.approche.objet.domain.valueObject.resource.exceptions.NotEnoughResourceException;
import projet.approche.objet.domain.valueObject.resource.exceptions.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class NeedsTest {
	@Test
	void testConstructor() {
		Resource gold = new Resource(ResourceType.GOLD, 10);
		Needs needs = new Needs(5, gold);
		assertEquals(5, needs.time);
		assertTrue(needs.resources.contains(gold));
	}

	@Test
	void testEquals() {
		Resource gold = new Resource(ResourceType.GOLD, 10);
		Needs needs1 = new Needs(5, gold);
		Needs needs2 = new Needs(5, gold);

		assertTrue(needs1.equals(needs2));
	}

	@Test
	void testAdd() {
		Resource gold = new Resource(ResourceType.GOLD, 10);
		Needs needs1 = new Needs(5, gold);
		Needs needs2 = new Needs(5, gold);

		Needs result = needs1.add(needs2);

		assertEquals(10, result.time);
		assertTrue(result.resources.contains(new Resource(ResourceType.GOLD, 20)));
	}

	@Test
	void testSub() {
		Resource gold10 = new Resource(ResourceType.GOLD, 10);
		Resource gold20 = new Resource(ResourceType.GOLD, 20);
		Needs needs1 = new Needs(10, gold20);
		Needs needs2 = new Needs(5, gold10);
		Needs result = null;
		try {
			result = needs1.sub(needs2);
		} catch (NotEnoughResourceException | ResourceNotFoundException | NotEnoughTimeException e) {
			fail("Should not throw exception");
			return;
		}
		assertEquals(5, result.time);
		assertTrue(result.resources.contains(new Resource(ResourceType.GOLD, 10)));
	}

	@Test
	void testIsAffordable() {
		Resource gold = new Resource(ResourceType.GOLD, 10);
		Needs needs = new Needs(5, gold);
		ResourceList resources = new ResourceList(List.of(new Resource(ResourceType.GOLD, 20)));

		assertTrue(needs.isAffordable(resources));
	}

	@Test
	void testGetMissingResources() {
		Resource gold = new Resource(ResourceType.GOLD, 20);
		Needs needs = new Needs(5, gold);
		ResourceList resources = new ResourceList(List.of(new Resource(ResourceType.GOLD, 10)));

		ResourceList result = needs.getMissingResources(resources);

		assertTrue(result.contains(new Resource(ResourceType.GOLD, 10)));
	}

	@Test
	void testGetRemainingResources() {
		Resource gold = new Resource(ResourceType.GOLD, 10);
		Needs needs = new Needs(5, gold);
		ResourceList resources = new ResourceList(List.of(new Resource(ResourceType.GOLD, 20)));

		ResourceList result = needs.getRemainingResources(resources);

		assertTrue(result.contains(new Resource(ResourceType.GOLD, 10)));

		Resource gold2 = new Resource(ResourceType.WOOD, 20);
		Needs needs2 = new Needs(5, gold2);
		ResourceList resources2 = new ResourceList(
				List.of(new Resource(ResourceType.GOLD, 10), new Resource(ResourceType.WOOD, 20)));

		ResourceList result2 = needs2.getRemainingResources(resources2);

		assertTrue(result2.contains(new Resource(ResourceType.GOLD, 10)));
		assertTrue(result2.size() == 1);

		Needs needs3 = new Needs(5, new ArrayList<>());
		ResourceList resources3 = new ResourceList(
				List.of(new Resource(ResourceType.GOLD, 10), new Resource(ResourceType.WOOD, 20)));

		ResourceList result3 = needs3.getRemainingResources(resources3);

		assertTrue(result3.contains(new Resource(ResourceType.GOLD, 10)));
		assertTrue(result3.contains(new Resource(ResourceType.WOOD, 20)));
	}

	@Test
	void testMultiply() {
		Resource gold = new Resource(ResourceType.GOLD, 10);
		Needs needs = new Needs(5, gold);

		Needs result = needs.multiply(2);

		assertEquals(10, result.time);
		assertTrue(result.resources.contains(new Resource(ResourceType.GOLD, 20)));
	}
}