package persistence.DAO;

import business.models.boats.BoatType;
import business.models.Sailor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SailorsDAO extends GenericDAO {
    private final String fileText;

    public SailorsDAO(String filePath) {
        super(filePath);
        this.fileText = readFile();
    }

    public List<Sailor> getSailors() {
        List<Sailor> sailors = new ArrayList<>();

        String[] lines = this.fileText.split("\n");

        for (int line = 1; line < lines.length; line++) {
            String[] attributes = lines[line].split(";");

            HashMap<BoatType, Integer> types = new HashMap<>();

            types.put(BoatType.Windsurf, Integer.parseInt(attributes[3]));
            types.put(BoatType.Optimist, Integer.parseInt(attributes[4]));
            types.put(BoatType.Laser, Integer.parseInt(attributes[5]));
            types.put(BoatType.PatiCatala, Integer.parseInt(attributes[6]));
            types.put(BoatType.HobieDragoon, Integer.parseInt(attributes[7]));
            types.put(BoatType.HobieCat, Integer.parseInt(attributes[8]));

            sailors.add(new Sailor(Integer.parseInt(attributes[0]), attributes[1], Float.parseFloat(attributes[2]),
                    types, Integer.parseInt(attributes[8])));
        }

        return sailors;
    }
}