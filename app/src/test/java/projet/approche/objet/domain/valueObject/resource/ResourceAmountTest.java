package projet.approche.objet.domain.valueObject.resource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceAmountTest {

	@Test
	void testConstructor() {
		ResourceAmount resourceAmount = new ResourceAmount(10);
		assertEquals(10, resourceAmount.value);
	}

	@Test
	void testConstructorNegative() {
		assertThrows(IllegalArgumentException.class, () -> new ResourceAmount(-10));
	}

	@Test
	void testAdd() {
		ResourceAmount resourceAmount1 = new ResourceAmount(10);
		ResourceAmount resourceAmount2 = new ResourceAmount(20);
		ResourceAmount result = resourceAmount1.add(resourceAmount2);
		assertEquals(30, result.value);
	}

	@Test
	void testSub() {
		ResourceAmount resourceAmount1 = new ResourceAmount(20);
		ResourceAmount resourceAmount2 = new ResourceAmount(10);
		ResourceAmount result = resourceAmount1.sub(resourceAmount2);
		assertEquals(10, result.value);
	}

	@Test
	void testEquals() {
		ResourceAmount resourceAmount1 = new ResourceAmount(10);
		ResourceAmount resourceAmount2 = new ResourceAmount(10);
		assertTrue(resourceAmount1.equals(resourceAmount2));
	}

	@Test
	void testIsGreaterOrEqual() {
		ResourceAmount resourceAmount1 = new ResourceAmount(10);
		ResourceAmount resourceAmount2 = new ResourceAmount(10);
		assertTrue(resourceAmount1.isGreaterOrEqual(resourceAmount2));
	}

	@Test
	void testIsLessOrEqual() {
		ResourceAmount resourceAmount1 = new ResourceAmount(10);
		ResourceAmount resourceAmount2 = new ResourceAmount(10);
		assertTrue(resourceAmount1.isLessOrEqual(resourceAmount2));
	}

	@Test
	void testIsLess() {
		ResourceAmount resourceAmount1 = new ResourceAmount(10);
		ResourceAmount resourceAmount2 = new ResourceAmount(20);
		assertTrue(resourceAmount1.isLess(resourceAmount2));
	}

	@Test
	void testIsGreater() {
		ResourceAmount resourceAmount1 = new ResourceAmount(20);
		ResourceAmount resourceAmount2 = new ResourceAmount(10);
		assertTrue(resourceAmount1.isGreater(resourceAmount2));
	}

	@Test
	void testToString() {
		ResourceAmount resourceAmount = new ResourceAmount(10);
		assertEquals("10", resourceAmount.toString());
	}

	@Test
	void testToShortString() {
		ResourceAmount resourceAmount1 = new ResourceAmount(1000);
		ResourceAmount resourceAmount2 = new ResourceAmount(1000000);
		ResourceAmount resourceAmount3 = new ResourceAmount(1000000000);
		assertEquals("1k", resourceAmount1.toShortString());
		assertEquals("1M", resourceAmount2.toShortString());
		assertEquals("1G", resourceAmount3.toShortString());
	}
}