package projet.approche.objet.domain.valueObject.needs;

import java.util.List;

import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;

public class Production extends Needs {
	public Production(int time, List<Resource> resources) {
		super(time, resources);
	}

	public Production(int time) {
		super(time);
	}

	protected Production(int time, ResourceList resources) {
		super(time, resources);
	}

	/**
	 * Multiplies the production by the specified multiplier. (only the resources,
	 * not the time)
	 *
	 * @param multiplier the multiplier to apply
	 * @return a new Production object with the multiplied production
	 */
	@Override
	public Production multiply(float multiplier) {
		var need = super.multiply(multiplier);
		return new Production(this.time, need.resources);
	}

	/**
	 * Add the production to the resources.
	 *
	 * @param toAdd the resources where the production will be added
	 * @return a new ResourceList with the production added
	 */
	public ResourceList harvestProduction(ResourceList toAdd) {
		return this.resources.add(toAdd);
	}

	@Override
	public String toString() {
		return "\nProduction per " + time + " day(s) : \n" + resources;
	}
}
