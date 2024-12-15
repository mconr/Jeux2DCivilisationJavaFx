package projet.approche.objet.domain.valueObject.needs;

import static projet.approche.objet.domain.valueObject.resource.ResourceType.GOLD;

import java.util.List;

import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;

public class ConstructionNeeds extends Needs {

	public final int goldAmountForConstruction;

	public ConstructionNeeds(int time, int goldAmount, List<Resource> resources) {
		super(time, new Resource(GOLD, goldAmount), resources);
		this.goldAmountForConstruction = goldAmount;
	}

	private ConstructionNeeds(int time, int goldAmount, ResourceList resources) {
		super(time, resources);
		this.goldAmountForConstruction = goldAmount;
	}

	public ConstructionNeeds multiply(float multiplier) {
		return new ConstructionNeeds(Math.round(this.time * multiplier),
				Math.round((this.goldAmountForConstruction * multiplier)),
				this.resources.multiply(multiplier));
	}

	@Override
	public String toString() {
		return "Construction cost : " + goldAmountForConstruction + " gold\nConstruction duration : " + time
				+ " days\nConstruction materials needed : \n" + resources;
	}
}
