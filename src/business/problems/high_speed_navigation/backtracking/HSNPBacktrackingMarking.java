package business.problems.high_speed_navigation.backtracking;

public class HSNPBacktrackingMarking {
    private HSNPBacktrackingBoatMarking[] sailorsByBoat;
    private double totalSpeed;
    private int fullBoatsCounter;

    public HSNPBacktrackingMarking(int boatsQty) {
        this.sailorsByBoat = new HSNPBacktrackingBoatMarking[boatsQty];

        for (int i = 0; i < boatsQty; i++) {
            this.sailorsByBoat[i] = new HSNPBacktrackingBoatMarking();
        }

        this.totalSpeed = 0;
        this.fullBoatsCounter = 0;
    }

    public HSNPBacktrackingBoatMarking[] getSailorsByBoat() {
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