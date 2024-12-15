package projet.approche.objet.domain.repository;

import java.io.IOException;

import projet.approche.objet.application.App;
import projet.approche.objet.domain.valueObject.game.GameStarter;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;

public interface Repository {

	void save(App app) throws NoBuildingHereException, NotInGridException, IOException;

	GameStarter load();

}
