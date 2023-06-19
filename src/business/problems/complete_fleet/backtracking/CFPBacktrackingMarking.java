package business.problems.complete_fleet.backtracking;

import business.models.boats.BoatType;

import java.util.HashMap;
import java.util.Map;

public class CFPBacktrackingMarking {
    private Map<BoatType, Integer> types;
    private int numberOfTypes;
    private int numberOfCentersUsed;

    public CFPBacktrackingMarking() {
        this.types = new HashMap<>();

        this.types.put(BoatType.Windsurf, 0);
        this.types.put(BoatType.Optimist, 0);
        this.types.put(BoatType.Laser, 0);
        this.types.put(BoatType.PatiCatala, 0);
        this.types.put(BoatType.HobieDragoon, 0);
        this.types.put(BoatType.HobieCat, 0);

        this.numberOfTypes = 0;
        this.numberOfCentersUsed = 0;
    }

    public Map<BoatType, Integer> getTypes() {
        return types;
    }

    public int getNumberOfTypes() {
        return numberOfTypes;
    }

    public int getNumberOfCentersUsed() {
        return numberOfCentersUsed;
    }

    public void addNumberOfTypes() {
        this.numberOfTypes++;
    }

    public void subtractNumberOfTypes() {
        this.numberOfTypes--;
    }

    public void addUsedCenter() {
        this.numberOfCentersUsed++;
    }

    public void subtractUsedCenter() {
        this.numberOfCentersUsed--;
    }

    public void addType(BoatType type) {
        this.types.put(type, this.types.get(type) + 1);
    }

    public void subtractType(BoatType type) {
        this.types.put(type, this.types.get(type) - 1);
    }
}
