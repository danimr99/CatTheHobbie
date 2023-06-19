package business.problems.high_speed_navigation.backtracking;

public class HSNPBacktrackingBoatMarking {
    private int sailorsOnBoard;
    private double speed;
    private boolean isFull;

    public HSNPBacktrackingBoatMarking() {
        this.sailorsOnBoard = 0;
        this.speed = 0;
        this.isFull = false;
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
