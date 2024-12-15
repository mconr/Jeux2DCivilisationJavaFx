package projet.approche.objet.domain.valueObject.needs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceType;

public class ConsumptionTest {

	@Test
	public void multiplyTest() {
		var consumtion = new Consumption(1);
		var multipliedConsumption = consumtion.multiply(2);
		assertTrue(multipliedConsumption.time == 1);
		assertTrue(multipliedConsumption.getResources().size() == 0);

		List<Resource> resources = List.of(new Resource(ResourceType.CEMENT, 2), new Resource(ResourceType.WOOD, 3));

		var consumtion2 = new Consumption(2, resources);
		var multipliedConsumption2 = consumtion2.multiply(3);
		assertTrue(multipliedConsumption2.time == 2);
		assertTrue(multipliedConsumption2.getResources().size() == 2);
		assertTrue(multipliedConsumption2.getResources().get(0).amount.value == 6);
		assertTrue(multipliedConsumption2.getResources().get(1).amount.value == 9);

	}

}
