package projet.approche.objet.domain.valueObject.resource;

public class Resource implements ResourceItf {

	public final ResourceType type;
	public final ResourceAmount amount;

	public Resource(ResourceType type, ResourceAmount amount) {
		this.type = type;
		this.amount = amount;
	}

	public Resource(ResourceType type, int amount) {
		this.type = type;
		this.amount = new ResourceAmount(amount);
	}

	public Resource add(Resource resource) {
		if (!this.type.equals(resource.type)) {
			throw new IllegalArgumentException("Cannot add resources of different types");
		}

		return new Resource(this.type, this.amount.add(resource.amount));
	}

	public Resource sub(Resource resource) {
		if (!this.type.equals(resource.type)) {
			throw new IllegalArgumentException("Cannot sub resources of different types");
		}

		return new Resource(this.type, this.amount.sub(resource.amount));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Resource) {
			Resource resource = (Resource) obj;
			return this.type.equals(resource.type) && this.amount.equals(resource.amount);
		}
		return false;
	}

	public boolean isSameType(Resource resource) {
		return this.type.equals(resource.type);
	}

	public boolean isGreaterOrEqual(Resource resource) {
		return this.amount.isGreaterOrEqual(resource.amount);
	}

	public boolean isLessOrEqual(Resource resource) {
		return this.amount.isLessOrEqual(resource.amount);
	}

	public boolean isLess(Resource resource) {
		return this.amount.isLess(resource.amount);
	}

	public boolean isGreater(Resource resource) {
		return this.amount.isGreater(resource.amount);
	}

	public boolean isSameAmount(Resource resource) {
		return this.amount.equals(resource.amount);
	}

	@Override
	public String toString() {
		return type.toString() + " : " + amount.toString();
	}

	public String toShortString() {
		return type.toString() + " : " + amount.toShortString();
	}

	public Resource multiply(float multiplier) {
		return new Resource(this.type, this.amount.multiply(multiplier));
	}

}
