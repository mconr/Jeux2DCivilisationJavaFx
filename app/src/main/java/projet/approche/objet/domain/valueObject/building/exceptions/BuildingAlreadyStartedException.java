package projet.approche.objet.domain.valueObject.building.exceptions;

public class BuildingAlreadyStartedException extends BuildingException {
	public BuildingAlreadyStartedException() {
		super();
	}

	public BuildingAlreadyStartedException(String message) {
		super(message);
	}

}
