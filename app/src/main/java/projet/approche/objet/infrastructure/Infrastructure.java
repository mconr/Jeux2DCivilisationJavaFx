package projet.approche.objet.infrastructure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.AbstractMap.SimpleEntry;

import projet.approche.objet.application.App;
import projet.approche.objet.domain.repository.Repository;
import projet.approche.objet.domain.valueObject.building.BuildingType;
import projet.approche.objet.domain.valueObject.game.GameStarter;
import projet.approche.objet.domain.valueObject.grid.Coordinate;
import projet.approche.objet.domain.valueObject.grid.exceptions.NoBuildingHereException;
import projet.approche.objet.domain.valueObject.grid.exceptions.NotInGridException;
import projet.approche.objet.domain.valueObject.resource.Resource;
import projet.approche.objet.domain.valueObject.resource.ResourceList;
import static projet.approche.objet.domain.valueObject.resource.ResourceType.*;

public class Infrastructure implements Repository {

	public void save(App app) throws NoBuildingHereException, NotInGridException, IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save CSV File");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
		fileChooser.setFileFilter(filter);
		File startingDirectory = new File("../app/src/main/resources/saves");
		fileChooser.setCurrentDirectory(startingDirectory);
		int userSelection = fileChooser.showSaveDialog(null);

		if (userSelection != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = fileChooser.getSelectedFile();
		String filePath = file.getAbsolutePath();
		if (!filePath.toLowerCase().endsWith(".csv")) {
			filePath += ".csv";
			file = new File(filePath);
		}

		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
			// Write the number of inhabitants and workers
			bufferedWriter.write(app.getManager().getInhabitantsInBuildings() + ","
					+ app.getManager().getWorkersInBuildings() + "\n");
			// Write the resources amounts
			bufferedWriter.write(app.getManager().getResources().getAmount(GOLD) + ","
					+ app.getManager().getResources().getAmount(FOOD) + ","
					+ app.getManager().getResources().getAmount(WOOD) + ","
					+ app.getManager().getResources().getAmount(STONE) + ","
					+ app.getManager().getResources().getAmount(COAL) + ","
					+ app.getManager().getResources().getAmount(IRON) + ","
					+ app.getManager().getResources().getAmount(STEEL) + ","
					+ app.getManager().getResources().getAmount(CEMENT) + ","
					+ app.getManager().getResources().getAmount(LUMBER) + ","
					+ app.getManager().getResources().getAmount(TOOLS) + "\n");
			// Write the size of the grid
			bufferedWriter.write(app.getManager().getGrid().getSize() + "\n");
			// Write the grid
			int size = app.getManager().getGrid().getSize();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					try {
						BuildingType buildingType = app.getManager().getGrid().getBuilding(new Coordinate(i, j)).type;
						bufferedWriter.write(i + "," + j + "," + buildingType.shortName + "\n");
					} catch (NoBuildingHereException e) {
						continue;
					} catch (NotInGridException e) {
						throw new RuntimeException(e); // should not happen
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public GameStarter load() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CSV files", "csv");
		chooser.setFileFilter(filter);
		File startingDirectory = new File("../app/src/main/resources/saves");
		chooser.setCurrentDirectory(startingDirectory);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File file = chooser.getSelectedFile();
		String line = "";
		String[] lineContents;
		String splitBy = ",";
		int inhabitants, workers;
		ResourceList startingResources;
		int size;
		List<SimpleEntry<Coordinate, BuildingType>> entries = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			line = br.readLine(); // reading number of inhabitants and workers
			lineContents = line.split(splitBy);
			inhabitants = Integer.parseInt(lineContents[0]);
			workers = Integer.parseInt(lineContents[1]);
			line = br.readLine(); // reading the resources amounts
			lineContents = line.split(splitBy);
			startingResources = new ResourceList(List.of(
					new Resource(GOLD, Integer.parseInt(lineContents[0])),
					new Resource(FOOD, Integer.parseInt(lineContents[1])),
					new Resource(WOOD, Integer.parseInt(lineContents[2])),
					new Resource(STONE, Integer.parseInt(lineContents[3])),
					new Resource(COAL, Integer.parseInt(lineContents[4])),
					new Resource(IRON, Integer.parseInt(lineContents[5])),
					new Resource(STEEL, Integer.parseInt(lineContents[6])),
					new Resource(CEMENT, Integer.parseInt(lineContents[7])),
					new Resource(LUMBER, Integer.parseInt(lineContents[8])),
					new Resource(TOOLS, Integer.parseInt(lineContents[9]))));
			line = br.readLine(); // reading the size of the grid
			size = Integer.parseInt(line);
			while ((line = br.readLine()) != null) // reading the grid
			{
				lineContents = line.split(splitBy);
				entries.add(new SimpleEntry<>(
						new Coordinate(Integer.parseInt(lineContents[0]), Integer.parseInt(lineContents[1])),
						BuildingType.fromShortString(lineContents[2])));
			}
			br.close();
			GameStarter gameStarter = new GameStarter(size, inhabitants, workers, startingResources, entries);
			return gameStarter;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
