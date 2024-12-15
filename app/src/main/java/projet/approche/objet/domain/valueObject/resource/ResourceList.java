package projet.approche.objet.domain.valueObject.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import projet.approche.objet.domain.valueObject.resource.exceptions.NotEnoughResourceException;
import projet.approche.objet.domain.valueObject.resource.exceptions.ResourceNotFoundException;

public class ResourceList implements Iterable<Resource> {

	private final List<Resource> resources;

	public ResourceList(final List<Resource> resources) {
		this.resources = resources;
	}

	public ResourceList() {
		this(new ArrayList<>());
	}

	public List<Resource> getResources() {
		return this.resources;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ResourceList) {
			ResourceList other = (ResourceList) obj;
			return this.resources.equals(other.resources);
		}
		return false;
	}

	/**
	 * Adds a resource to the list, if the list already have the same resourceType
	 * only add the amount.
	 *
	 * @param resource the resource to add
	 * @return a new ResourceList object with the added resource
	 */
	public ResourceList add(Resource resource) {
		List<Resource> copy = new ArrayList<>();
		boolean added = false;
		for (Resource r : this) {
			if (r.isSameType(resource)) {
				copy.add(r.add(resource));
				added = true;
			} else {
				copy.add(r);
			}
		}
		if (!added) {
			copy.add(resource);
		}
		return new ResourceList(copy);
	}

	/**
	 * Adds a list of resources to the list.
	 *
	 * @param resources the list of resources to add
	 * @return a new ResourceList object with the added resources
	 */
	public ResourceList add(ResourceList resources) {
		ResourceList result = new ResourceList();
		for (Resource resource : this) {
			result = result.add(resource);
		}
		for (Resource resource : resources) {
			result = result.add(resource);
		}
		return result;
	}

	/**
	 * Removes a resource from the list.
	 *
	 * @param toRemove the resource to remove
	 * @return a new ResourceList object with the removed resource
	 * @throws NotEnoughResourceException
	 * @throws ResourceNotFoundException
	 */
	public ResourceList remove(Resource toRemove) throws NotEnoughResourceException, ResourceNotFoundException {
		int size = this.resources.size();
		List<Resource> copy = new ArrayList<>();
		boolean found = false;
		for (int i = 0; i < size; i++) {
			if (this.resources.get(i).isSameType(toRemove)) {
				if (this.resources.get(i).isGreaterOrEqual(toRemove)) {
					found = true;
					copy.add(this.resources.get(i).sub(toRemove));
				} else {
					throw new NotEnoughResourceException("Not enough resource to remove");
				}
			} else {
				copy.add(this.resources.get(i));
			}
		}
		if (!found) {
			throw new ResourceNotFoundException("The resource was not found");
		}
		return new ResourceList(copy);
	}

	/**
	 * Removes a list of resources from the list.
	 *
	 * @param toRemove the list of resources to remove
	 * @return a new ResourceList object with the removed resources
	 * @throws ResourceNotFoundException
	 * @throws NotEnoughResourceException
	 */
	public ResourceList remove(ResourceList toRemove) throws NotEnoughResourceException, ResourceNotFoundException {
		ResourceList result = new ResourceList();
		for (Resource resource : this) {
			result = result.add(resource);
		}
		for (Resource resource : toRemove) {
			result = result.remove(resource);
		}
		return result;
	}

	/**
	 * Checks if the list contains the resource.
	 *
	 * @param resource the resource to check
	 * @return true if the list contains the resource, false otherwise
	 */
	public boolean contains(Resource resource) {
		return this.resources.contains(resource);
	}

	/**
	 * Checks if the list contains all the resources.
	 *
	 * @param resources the resources to check
	 * @return true if the list contains all the resources, false otherwise
	 */
	public boolean contains(ResourceList resources) {
		return this.resources.containsAll(resources.resources);
	}

	public boolean isEmpty() {
		return this.resources.isEmpty();
	}

	public int size() {
		return this.resources.size();
	}

	public Resource get(int index) {
		return this.resources.get(index);
	}

	public ResourceAmount getAmount(ResourceType type) {
		for (Resource resource : this.resources) {
			if (resource.type.equals(type)) {
				return resource.amount;
			}
		}
		return new ResourceAmount(0);
	}

	public Resource get(ResourceType type) {
		for (Resource resource : this.resources) {
			if (resource.type.equals(type)) {
				return resource;
			}
		}
		return null;
	}

	@Override
	public Iterator<Resource> iterator() {
		return this.resources.iterator();
	}

	@Override
	public String toString() {
		String str = "";
		for (Resource resource : this.resources) {
			str += "  - " + resource.toString() + "\n";
		}
		return str;
	}

	public ResourceList multiply(float multiplier) {
		List<Resource> copy = new ArrayList<>();
		for (Resource resource : this) {
			copy.add(resource.multiply(multiplier));
		}
		return new ResourceList(copy);
	}
}
