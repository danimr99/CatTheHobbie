package business.problems.high_speed_navigation.backtracking;

import business.models.Sailor;
import business.models.boats.Boat;
import business.models.methods.Backtracking;
import presentation.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HSNPBacktracking extends Backtracking {
    private static final int SAILOR_UNASSIGNED = -2;
    private static final int SAILOR_ASSIGNED_NONE = -1;
    private final Console console;
    private final List<Boat> boats;
    private final List<Sailor> sailors;
    private final boolean useMarking;
    private final boolean usePbmsc;
    private double previousSpeed;
    private double bestTotalSpeed;
    private int solutionsFoundCounter;
    private int[] bestConfiguration;

    public HSNPBacktracking(List<Boat> boats, List<Sailor> sailors, boolean useImprovements) {
        this.console = new Console();
        this.boats = boats;
        this.sailors = sailors;
        this.useMarking = useImprovements;
        this.usePbmsc = useImprovements;
        this.solutionsFoundCounter = 0;
        this.bestConfiguration = new int[this.sailors.size()];
        this.bestTotalSpeed = 0;
    }

    @Override
    public void run() {
        // Set up initial configuration
        int[] x = new int[this.sailors.size()];
        int k = 0;

        if (this.useMarking) {
            HSNPBacktrackingMarking marking = new HSNPBacktrackingMarking(this.boats.size());
            this.backtracking(x, k, marking);
        } else {
            this.backtracking(x, k, null);
        }

        if (this.bestConfiguration != null) {
            this.console.showMessage("Solutions found: " + this.solutionsFoundCounter, true);
            this.console.spacing();
            this.console.showMessage("Best configuration: ", true);

            for (int i = 0; i < this.boats.size(); i++) {
                this.console.showMessage("Boat " + (i + 1) + ": "+ this.boats.get(i).getName(), true);

                for (int j = 0; j < this.sailors.size(); j++) {
                    if (this.bestConfiguration[j] == i) {
                        this.console.showMessage("\tSailor " + this.sailors.get(j).getName(), true);
                    }
                }
            }

            this.console.spacing();
            this.console.showMessage("Total speed: " + this.bestTotalSpeed, true);
        } else {
            this.console.showError("There is not a solution", true);
        }
    }

    public void backtracking(int[] x, int k, HSNPBacktrackingMarking m) {
        this.prepare(x, k);

        while (this.existsSuccessor(x, k)) {
            this.existsNext(x, k);

            if (this.useMarking) {
                this.mark(x, k, m);
            }

            if (this.solution(x, k)) {
                if (this.useMarking) {
                    if (this.markedFeasible(m)) {
                        this.solutionsFoundCounter++;
                        this.markedTreatSolution(x, m);
                    }
                } else {
                    if (this.feasible(x)) {
                        this.solutionsFoundCounter++;
                        this.treatSolution(x);
                    }
                }
            } else {
                if (this.useMarking) {
                    if (this.markedCompletable(x, k, m)) {
                        if (this.usePbmsc) {
                            if (m.getTotalSpeed() > this.bestTotalSpeed) {
                                this.backtracking(x, k + 1, m);
                            }
                        } else {
                            this.backtracking(x, k + 1, m);
                        }
                    }
                } else {
                    if (this.completable(x, k)) {
                        this.backtracking(x, k + 1, m);
                    }
                }
            }

            if (this.useMarking) {
                this.unmark(x, k, m);
            }
        }
    }

    @Override
    protected void prepare(int[] x, int k) {
        x[k] = HSNPBacktracking.SAILOR_UNASSIGNED;
    }

    @Override
    protected boolean existsSuccessor(int[] x, int k) {
        return x[k] < (this.boats.size() - 1);
    }

    @Override
    protected void existsNext(int[] x, int k) {
        x[k]++;
    }

    @Override
    protected boolean solution(int[] x, int k) {
        return k == (this.sailors.size() - 1);
    }

    @Override
    protected boolean completable(int[] x, int k) {
        int counter = 0;

        if (x[k] == HSNPBacktracking.SAILOR_ASSIGNED_NONE) {
            return true;
        }

        for (int i = 0; i <= k; i++) {
            if (x[i] == x[k]) {
                counter++;
            }
        }

        return this.boats.get(x[k]).getCapacity() >= counter;
    }

    @Override
    protected boolean feasible(int[] x) {
        int[] sailorsByShip = new int[this.boats.size()];

        // Count sailors by ship
        for (int i = 0; i < this.sailors.size(); i++) {
            if (x[i] > HSNPBacktracking.SAILOR_ASSIGNED_NONE) {
                sailorsByShip[x[i]]++;
            }
        }

        // Check if all boats are full
        for (int i = 0; i < this.boats.size(); i++) {
            if (this.boats.get(i).getCapacity() > sailorsByShip[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void treatSolution(int[] x) {
        List<Sailor> sailorsByBoat = new ArrayList<>();
        double boatSpeed = 1;
        double totalSpeed = 0;

        for (int i = 0; i < this.boats.size(); i++) {
            Boat boat = this.boats.get(i);

            for (int j = 0; j < this.sailors.size(); j++) {
                if (x[j] == i) {
                    sailorsByBoat.add(this.sailors.get(j));
                }
            }

            for (Sailor sailor : sailorsByBoat) {
                boatSpeed *= sailor.getImpact(boat);
            }

            boatSpeed *= boat.getSpeed();
            totalSpeed += boatSpeed;
            sailorsByBoat.clear();
        }

        if (totalSpeed > this.bestTotalSpeed) {
            this.bestTotalSpeed = totalSpeed;
            this.bestConfiguration = Arrays.copyOf(x, this.sailors.size());
        }
    }

    private void mark(int[] x, int k, HSNPBacktrackingMarking m) {
        double boatSpeed = 1;

        // Check if sailor is assigned to a boat
        if (x[k] != HSNPBacktracking.SAILOR_ASSIGNED_NONE) {
            m.getSailorsByBoat()[x[k]].setSailorsOnBoard(m.getSailorsByBoat()[x[k]].getSailorsOnBoard() + 1);
            m.getSailorsByBoat()[x[k]].setFull(this.boats.get(x[k]).getCapacity() <=
                    m.getSailorsByBoat()[x[k]].getSailorsOnBoard());

            if (m.getSailorsByBoat()[x[k]].isFull() && this.boats.get(x[k]).getCapacity() ==
                    m.getSailorsByBoat()[x[k]].getSailorsOnBoard()) {
                m.setFullBoatsCounter(m.getFullBoatsCounter() + 1);
            }

            // Save previous speed to unmark later on
            this.previousSpeed = m.getSailorsByBoat()[x[k]].getSpeed();

            if (m.getSailorsByBoat()[x[k]].getSpeed() != 0) {
                m.setTotalSpeed(m.getTotalSpeed() - this.previousSpeed);
            }

            // Update total speed
            Boat boat = this.boats.get(x[k]);

            for (int i = k; i >= 0; i--) {
                if (x[i] == x[k]) {
                    boatSpeed *= this.sailors.get(i).getImpact(boat);
                }
            }

            boatSpeed *= boat.getSpeed();
            m.getSailorsByBoat()[x[k]].setSpeed(boatSpeed);
            m.setTotalSpeed(m.getTotalSpeed() + boatSpeed);
        }
    }

    private void unmark(int[] x, int k, HSNPBacktrackingMarking m) {
        boolean full = false;

        if (x[k] != HSNPBacktracking.SAILOR_ASSIGNED_NONE) {
            m.getSailorsByBoat()[x[k]].setSailorsOnBoard(m.getSailorsByBoat()[x[k]].getSailorsOnBoard() - 1);

            if (m.getSailorsByBoat()[x[k]].isFull()) {
                full = true;
            }

            if (m.getSailorsByBoat()[x[k]].getSailorsOnBoard() < this.boats.get(x[k]).getCapacity()) {
                m.getSailorsByBoat()[x[k]].setFull(false);

                if (full) {
                    m.setFullBoatsCounter(m.getFullBoatsCounter() - 1);
                }
            }

            m.setTotalSpeed(m.getTotalSpeed() - m.getSailorsByBoat()[x[k]].getSpeed());
            m.getSailorsByBoat()[x[k]].setSpeed(this.previousSpeed);
            m.setTotalSpeed(m.getTotalSpeed() + m.getSailorsByBoat()[x[k]].getSpeed());
        }

    }

    public boolean markedCompletable(int[] x, int k, HSNPBacktrackingMarking m) {
        if (x[k] == HSNPBacktracking.SAILOR_ASSIGNED_NONE) {
            return true;
        }

        return this.boats.get(x[k]).getCapacity() >= m.getSailorsByBoat()[x[k]].getSailorsOnBoard();
    }

    public boolean markedFeasible(HSNPBacktrackingMarking m) {
        return m.getFullBoatsCounter() == this.boats.size();
    }

    private void markedTreatSolution(int[] x, HSNPBacktrackingMarking m) {
        if (m.getTotalSpeed() > this.bestTotalSpeed) {
            this.bestTotalSpeed = m.getTotalSpeed();
            this.bestConfiguration = Arrays.copyOf(x, x.length);
        }
    }
}
