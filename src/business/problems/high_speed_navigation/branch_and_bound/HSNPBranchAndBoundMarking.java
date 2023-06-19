package business.problems.high_speed_navigation.branch_and_bound;


public class HSNPBranchAndBoundMarking {
    private HSNPBranchAndBoundBoatMarking[] sailorsByBoat;
    private double totalSpeed;
    private int fullBoatsCounter;

    public HSNPBranchAndBoundMarking(int boatsQty) {
        this.sailorsByBoat = new HSNPBranchAndBoundBoatMarking[boatsQty];

        for (int i = 0; i < boatsQty; i++) {
            this.sailorsByBoat[i] = new HSNPBranchAndBoundBoatMarking();
        }

        this.totalSpeed = 0;
        this.fullBoatsCounter = 0;
    }

    public HSNPBranchAndBoundBoatMarking[] getSailorsByBoat() {
        return sailorsByBoat;
    }

    public double getTotalSpeed() {
        return totalSpeed;
    }

    public void setTotalSpeed(double totalSpeed) {
        this.totalSpeed = totalSpeed;
    }

    public int getFullBoatsCounter() {
        return fullBoatsCounter;
    }

    public void setFullBoatsCounter(int fullBoatsCounter) {
        this.fullBoatsCounter = fullBoatsCounter;
    }
}
