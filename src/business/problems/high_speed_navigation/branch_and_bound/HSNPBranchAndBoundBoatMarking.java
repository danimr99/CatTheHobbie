package business.problems.high_speed_navigation.branch_and_bound;

public class HSNPBranchAndBoundBoatMarking {
    private int sailorsOnBoard;
    private double speed;
    private boolean isFull;

    public HSNPBranchAndBoundBoatMarking() {
        this.sailorsOnBoard = 0;
        this.speed = 0;
        this.isFull = false;
    }

    public HSNPBranchAndBoundBoatMarking(HSNPBranchAndBoundBoatMarking copy) {
        this.sailorsOnBoard = copy.getSailorsOnBoard();
        this.speed = copy.getSpeed();
        this.isFull = copy.isFull();
    }

    public int getSailorsOnBoard() {
        return sailorsOnBoard;
    }

    public void setSailorsOnBoard(int sailorsOnBoard) {
        this.sailorsOnBoard = sailorsOnBoard;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }
}
