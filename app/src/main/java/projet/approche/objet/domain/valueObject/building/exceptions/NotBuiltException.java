package projet.approche.objet.domain.valueObject.building.exceptions;

public class NotBuiltException extends BuildingException {
	public NotBuiltException() {
		this("");
	}

	public NotBuiltException(String message) {
		super("not built " + message);
	}

}
