package persistence.DAO;

import business.models.boats.Boat;
import business.models.boats.BoatType;

import java.util.ArrayList;
import java.util.List;

public class BoatsDAO extends GenericDAO {
    public BoatsDAO(String filePath) {
        super(filePath);
        this.fileText = this.readFile();
    }

    public List<Boat> getBoats() {
        List<Boat> boats = new ArrayList<>();

        String[] lines = this.fileText.split("\n");

        for (int line = 1; line < lines.length; line++) {
            String[] attributes = lines[line].split(";");


            if (!attributes[7].equalsIgnoreCase("broken") &&
                    !attributes[7].equalsIgnoreCase("unavailable")) {
                boats.add(new Boat(Integer.parseInt(attributes[0]), attributes[1], BoatType.getEnumValue(attributes[2]),
                        Float.parseFloat(attributes[3]), Float.parseFloat(attributes[4]),
                        Integer.parseInt(attributes[5]), Integer.parseInt(attributes[6]),
                        attributes[7], Integer.parseInt(attributes[8]), attributes[9])
                );
            }
        }

        return boats;
    }
}
