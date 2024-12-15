package projet.approche.objet.domain.valueObject.needs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceType;

public class ProductionTest {

	@Test
	public void multiplyTest() {
		var production = new Production(1);
		var multipliedProduction = production.multiply(2);
		assert (multipliedProduction.time == 1);
		assert (multipliedProduction.resources.getResources().size() == 0);

		List<Resource> resources = List.of(new Resource(ResourceType.CEMENT, 2), new Resource(ResourceType.WOOD, 3));

		var production2 = new Production(2, resources);
		var multipliedProduction2 = production2.multiply(3);
		assertTrue(multipliedProduction2.time == 2);
		assertTrue(multipliedProduction2.resources.getResources().size() == 2);
		assertTrue(multipliedProduction2.resources.getResources().get(0).amount.value == 6);
		assertTrue(multipliedProduction2.resources.getResources().get(1).amount.value == 9);
	}

}
