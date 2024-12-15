package projet.approche.objet.domain.valueObject.resource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import projet.approche.objet.domain.valueObject.resource.exceptions.NotEnoughResourceException;
import projet.approche.objet.domain.valueObject.resource.exceptions.ResourceNotFoundException;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourceListTest {

	private static Resource gold;
	private static ResourceList resourceList;

	@BeforeAll
	static void setUp() {
		gold = new Resource(ResourceType.GOLD, 10);
		resourceList = new ResourceList(List.of(gold));
	}

	@Test
	void testConstructor() {
		assertEquals(1, resourceList.size());
		assertTrue(resourceList.contains(gold));
	}

	@Test
	void testEquals() {
		ResourceList resourceList2 = new ResourceList(List.of(gold));
		assertTrue(resourceList.equals(resourceList2));
	}

	@Test
	void testAddResource() {
		Resource gold = new Resource(ResourceType.GOLD, 10);
		ResourceList resourceList = new ResourceList();
		ResourceList result = resourceList.add(gold);
		assertEquals(1, result.size());
		assertTrue(result.contains(gold));
	}

	@Test
	void testAddSameResource() {
		Resource gold20 = new Resource(ResourceType.GOLD, 20);
		ResourceList result = resourceList.add(gold20);
		assertEquals(1, result.size());
		assertTrue(result.contains(new Resource(ResourceType.GOLD, 30)));
	}

	@Test
	void testAddResourceList() {
		ResourceList resourceList2 = new ResourceList();
		ResourceList result = resourceList2.add(resourceList);
		assertEquals(1, result.size());
		assertTrue(result.contains(gold));
	}

	@Test
	void testRemoveResource() throws NotEnoughResourceException, ResourceNotFoundException {
		ResourceList result = resourceList.remove(gold);
		assertEquals(1, result.size());
		assertFalse(result.contains(gold));
		assertEquals(0, result.get(0).amount.value);
	}

	@Test
	void testRemoveResourceNotEnough() throws NotEnoughResourceException, ResourceNotFoundException {
		Resource gold20 = new Resource(ResourceType.GOLD, 20);
		ResourceList resourceList = new ResourceList(List.of(gold20));
		ResourceList result = resourceList.remove(gold);
		assertEquals(1, result.size());
		assertTrue(result.contains(gold));
	}

	@Test
	void testRemoveResourceNotSameType() throws NotEnoughResourceException, ResourceNotFoundException {
		Resource wood = new Resource(ResourceType.WOOD, 10);
		assertThrows(ResourceNotFoundException.class, () -> resourceList.remove(wood));
	}

	@Test
	void testRemoveResourceEnough() throws NotEnoughResourceException, ResourceNotFoundException {
		Resource gold20 = new Resource(ResourceType.GOLD, 20);
		ResourceList resourceList = new ResourceList(List.of(gold20));
		ResourceList result = resourceList.remove(gold);
		assertEquals(1, result.size());
		assertTrue(result.contains(new Resource(ResourceType.GOLD, 10)));
	}

	@Test
	void testRemoveResourceList() throws NotEnoughResourceException, ResourceNotFoundException {
		ResourceList resourceList2 = new ResourceList(List.of(gold));
		ResourceList result = resourceList2.remove(resourceList);
		assertEquals(1, result.size());
		assertFalse(result.contains(gold));
		assertEquals(0, result.get(0).amount.value);
	}

	@Test
	void testRemoveResourceListNotEnough() {
		Resource gold20 = new Resource(ResourceType.GOLD, 20);
		ResourceList resourceList2 = new ResourceList(List.of(gold20));
		assertThrows(NotEnoughResourceException.class, () -> resourceList.remove(resourceList2));
	}

	@Test
	void testRemoveResourceListNotSameType() {
		Resource wood = new Resource(ResourceType.WOOD, 10);
		ResourceList resourceList2 = new ResourceList(List.of(wood));
		assertThrows(ResourceNotFoundException.class, () -> resourceList.remove(resourceList2));
	}

	@Test
	void testContainsResource() {
		assertTrue(resourceList.contains(gold));
	}

	@Test
	void testContainsResourceList() {
		ResourceList resourceList2 = new ResourceList(List.of(gold));
		assertTrue(resourceList2.contains(resourceList));
	}

	@Test
	void testIsEmpty() {
		ResourceList resourceList = new ResourceList();
		assertTrue(resourceList.isEmpty());
	}

	@Test
	void testSize() {
		assertEquals(1, resourceList.size());
	}

	@Test
	void testGet() {
		assertEquals(gold, resourceList.get(0));
	}

	@Test
	void testIterator() {
		Iterator<Resource> iterator = resourceList.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(gold, iterator.next());
	}
}