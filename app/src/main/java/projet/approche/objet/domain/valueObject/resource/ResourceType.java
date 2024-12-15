package projet.approche.objet.domain.valueObject.resource;

public enum ResourceType implements ResourceItf {
	GOLD("Gold", "G", 10),
	FOOD("Food", "F", 1.5f),
	WOOD("Wood", "W", 1),
	STONE("Stone", "S", 1.5f),
	COAL("Coal", "C", 2.5f),
	IRON("Iron", "I", 3),
	STEEL("Steel", "St", 5),
	CEMENT("Cement", "Ce", 6),
	LUMBER("Lumber", "L", 7),
	TOOLS("Tools", "T", 10),
	INHABITANTS("Inhabitants", "P", 0),
	WORKERS("Workers", "W", 0);

	public final String name;
	public final String shortName;
	public final float valueMultiplier;

	private ResourceType(String name, String shortName, float value) {
		this.name = name;
		this.shortName = shortName;
		this.valueMultiplier = value;
	}

	@Override
	public String toString() {
		return name;
	}

	public String toShortString() {
		return shortName;
	}

	public static ResourceType fromString(String name) {
		for (ResourceType resourceType : ResourceType.values()) {
			if (resourceType.name.equals(name)) {
				return resourceType;
			}
		}
		throw new IllegalArgumentException("No resource type with name " + name);
	}
}
