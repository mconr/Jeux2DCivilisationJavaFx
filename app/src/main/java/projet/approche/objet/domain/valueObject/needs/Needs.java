package projet.approche.objet.domain.valueObject.needs;

import java.util.List;

import projet.approche.objet.domain.valueObject.needs.exceptions.NotEnoughTimeException;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;
import projet.approche.objet.domain.valueObject.resource.exceptions.NotEnoughResourceException;
import projet.approche.objet.domain.valueObject.resource.exceptions.ResourceNotFoundException;

/**
 * Needs in time and resources
 */
public class Needs {
	public final ResourceList resources;
	public final int time;

	public Needs(int time, List<Resource> resources) {
		this.resources = new ResourceList(resources);
		this.time = time;
	}

	public Needs(int time, Resource gold, List<Resource> resources) {
		var tmp = new ResourceList(resources);
		this.resources = tmp.add(gold);
		this.time = time;
	}

	public Needs(int time, Resource singleResource) {
		this.resources = new ResourceList(List.of(singleResource));
		this.time = time;
	}

	public Needs(int time) {
		this.resources = new ResourceList();
		this.time = time;
	}

	protected Needs(int time, ResourceList resources) {
		this.resources = resources;
		this.time = time;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Needs) {
			Needs needs = (Needs) obj;
			return this.time == needs.time && this.resources.equals(needs.resources);
		}
		return false;
	}

	public Needs add(Needs needs) {
		return new Needs(this.time + needs.time, this.resources.add(needs.resources));
	}

	public Needs sub(Needs needs) throws NotEnoughResourceException, ResourceNotFoundException, NotEnoughTimeException {
		if (this.time < needs.time)
			throw new NotEnoughTimeException("Not enough time");
		return new Needs(this.time - needs.time, this.resources.remove(needs.resources));
	}

	public ResourceList getResources() {
		return this.resources;
	}

	/**
	 * Checks if the resources are affordable.
	 *
	 * @param resources the resources available
	 * @return true if the resources are affordable, false otherwise
	 */
	public boolean isAffordable(ResourceList resources) {
		int cpt = 0;
		for (Resource resource : resources) {
			for (Resource needs : this.resources) {
				if (resource.type.equals(needs.type)) {
					if (resource.amount.isLess(needs.amount))
						return false;
					else
						cpt++;

				}
			}
		}
		if (cpt == this.resources.size())
			return true;
		return false;
	}

	/**
	 * Gets the missing resources.
	 *
	 * @param resources the resources available
	 * @return the missing resources
	 */
	public ResourceList getMissingResources(ResourceList resources) {
		ResourceList missingResources = new ResourceList();
		for (Resource needs : this.resources) {
			for (Resource resource : resources) {
				if (resource.type.equals(needs.type)) {
					if (resource.amount.isLess(needs.amount))
						missingResources = missingResources
								.add(new Resource(needs.type, needs.amount.sub(resource.amount)));
				}
			}
		}
		return missingResources;
	}

	/**
	 * Gets the remaining resources.
	 *
	 * @param resources the resources available
	 * @return the remaining resources
	 */
	public ResourceList getRemainingResources(ResourceList resources) {
		if (this.resources.isEmpty())
			return resources;
		ResourceList remainingResources = new ResourceList();
		for (Resource resource : resources) {
			boolean found = false;
			for (Resource needs : this.resources) {
				if (resource.type.equals(needs.type)) {
					if (resource.amount.isGreater(needs.amount))
						remainingResources = remainingResources
								.add(new Resource(needs.type, resource.amount.sub(needs.amount)));
					found = true;
				}
			}
			if (!found) {
				remainingResources = remainingResources.add(resource);
			}
		}
		return remainingResources;
	}

	/*
	 * Multiply the amount of each resource. Increased production and consumption
	 */
	public Needs multiply(float multiplier) {
		return new Needs(Math.round((this.time * multiplier)), this.resources.multiply(multiplier));
	}

	@Override
	public String toString() {
		return "[resources=" + resources + ", time=" + time + "]";
	}
}
