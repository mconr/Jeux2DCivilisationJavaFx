package projet.approche.objet.domain.valueObject.building.exceptions;

public class BuildingException extends Exception {
	public BuildingException() {
		this("");
	}

	public BuildingException(String message) {
		super("Building exception: " + message);
	}

}
