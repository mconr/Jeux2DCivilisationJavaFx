package projet.approche.objet.domain.valueObject.building;

public enum Upgrade {

	UPGRADECOST(0.75f),
	UPGRADEPRODUCTION(2f),
	UPGRADECONSUMPTION(2f),
	UPGRADEINHABITANTS(1.5f),
	UPGRADEWORKERS(1.5f);

	public final float value;

	private Upgrade(float value) {
		this.value = value;
	}

	public float getMultiplierByLevel(int level) {
		if (level <= 1) {
			return 1;
		}
		float multiplier = 0;
		for (int i = 1; i < level; i++) {
			multiplier += this.value;
		}
		return multiplier;
	}
}
