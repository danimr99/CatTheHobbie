package business.managers;

import business.models.Center;
import business.models.boats.Boat;
import persistence.DAO.BoatsDAO;

import java.util.*;

public class BoatsManager {
    private final List<Boat> boats;
    private final List<Center> centers;

    public BoatsManager(String filePath) {
        this.boats = new BoatsDAO(filePath).getBoats();
        this.centers = this.getDistinctCentersFromBoats();
    }

    public List<Boat> getBoats() {
        return boats;
    }

    private List<Center> getDistinctCentersFromBoats() {
        List<String> centerNames = new ArrayList<>();
        List<Center> centers = new ArrayList<>();

        for (Boat boat : this.boats) {
            if (!centerNames.contains(boat.getCenter())) {
                centerNames.add(boat.getCenter());

                Center center = new Center(boat.getCenter());
                center.addBoat(boat);

                centers.add(center);
            } else {
                centers.get(centerNames.indexOf(boat.getCenter())).addBoat(boat);
            }
        }

        return centers;
    }

    public List<Center> getCenters() {
        return centers;
    }
}