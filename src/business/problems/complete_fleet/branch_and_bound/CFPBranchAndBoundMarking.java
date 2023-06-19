package business.problems.complete_fleet.branch_and_bound;

import business.models.boats.BoatType;

import java.util.HashMap;
import java.util.Map;

public class CFPBranchAndBoundMarking {
    private int numberOfCentersUsed;
    private Map<BoatType, Integer> typesUsed;
    private int typesUsedCounter;

    public CFPBranchAndBoundMarking() {
        this.numberOfCentersUsed = 0;
        this.typesUsedCounter = 0;
        this.typesUsed = new HashMap<>();

        this.typesUsed.put(BoatType.Windsurf, 0);
        this.typesUsed.put(BoatType.Optimist, 0);
        this.typesUsed.put(BoatType.Laser, 0);
        this.typesUsed.put(BoatType.PatiCatala, 0);
        this.typesUsed.put(BoatType.HobieDragoon, 0);
        this.typesUsed.put(BoatType.HobieCat, 0);
    }

    public int getNumberOfCentersUsed() {
        return numberOfCentersUsed;
    }

    public Map<BoatType, Integer> getTypesUsed() {
        return typesUsed;
    }

    public int getTypesUsedCounter() {
        return typesUsedCounter;
    }

    public void setNumberOfCentersUsed(int numberOfCentersUsed) {
        this.numberOfCentersUsed = numberOfCentersUsed;
    }

    public void setTypesUsed(Map<BoatType, Integer> typesUsed) {
        this.typesUsed = typesUsed;
    }

    public void setTypesUsedCounter(int typesUsedCounter) {
        this.typesUsedCounter = typesUsedCounter;
    }
}
