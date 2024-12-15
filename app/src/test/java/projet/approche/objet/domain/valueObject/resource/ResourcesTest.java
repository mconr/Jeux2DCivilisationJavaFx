package projet.approche.objet.domain.valueObject.resource;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourcesTest {
	@Test
	void testConstructor() {
		ResourceType type = ResourceType.WOOD;
		ResourceAmount amount = new ResourceAmount(10);
		Resource resource = new Resource(type, amount);

		assertEquals(type, resource.type);
		assertEquals(amount, resource.amount);
	}

	@Test
	void testAdd() {
		ResourceType type = ResourceType.WOOD;
		Resource resource1 = new Resource(type, 10);
		Resource resource2 = new Resource(type, 20);

		Resource result = resource1.add(resource2);

		assertEquals(type, result.type);
		assertEquals(30, result.amount.value);
	}

	@Test
	void testAddDifferentTypes() {
		Resource resource1 = new Resource(ResourceType.WOOD, 10);
		Resource resource2 = new Resource(ResourceType.STONE, 20);

		assertThrows(IllegalArgumentException.class, () -> resource1.add(resource2));
	}

	@Test
	void testSub() {
		ResourceType type = ResourceType.WOOD;
		Resource resource1 = new Resource(type, 30);
		Resource resource2 = new Resource(type, 10);

		Resource result = resource1.sub(resource2);

		assertEquals(type, result.type);
		assertEquals(20, result.amount.value);
	}

	@Test
	void testEquals() {
		ResourceType type = ResourceType.WOOD;
		Resource resource1 = new Resource(type, 10);
		Resource resource2 = new Resource(type, 10);

		assertTrue(resource1.equals(resource2));
	}

	@Test
	void testToString() {
		ResourceType type = ResourceType.WOOD;
		Resource resource = new Resource(type, 10);

		assertEquals("Wood : 10", resource.toString());
	}

	@Test
	void testToShortString() {
		ResourceType type = ResourceType.WOOD;
		Resource resource = new Resource(type, 10);

		assertEquals("Wood : 10", resource.toShortString());
	}
}
