package projet.approche.objet.domain.valueObject.needs;

import java.util.List;

import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;

public class Consumption extends Needs {
	public Consumption(int time, List<Resource> resources) {
		super(time, resources);
	}

	public Consumption(int time) {
		super(time);
	}

	protected Consumption(int time, ResourceList resources) {
		super(time, resources);
	}

	/**
	 * Multiplies the consumption by the specified multiplier. (only the resources,
	 * not the time)
	 *
	 * @param multiplier the multiplier to apply
	 * @return a new Consumption object with the multiplied consumption
	 */
	@Override
	public Consumption multiply(float multiplier) {
		var need = super.multiply(multiplier);
		return new Consumption(this.time, need.resources);
	}

	@Override
	public String toString() {
		return "\nConsumption per " + time + " day(s) : \n" + resources;
	}
}
