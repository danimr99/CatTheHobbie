package business.problems.high_speed_navigation.branch_and_bound;

import business.models.Sailor;
import business.models.boats.Boat;
import business.models.methods.branch_and_bound.BranchAndBound;
import business.models.methods.branch_and_bound.CommonConfiguration;
import presentation.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class HSNPBranchAndBound extends BranchAndBound {
    private static final int SAILOR_ASSIGNED_NONE = -1;
    private final Console console;
    private final List<Boat> boats;
    private final List<Sailor> sailors;
    private final boolean useMarking;
    private int solutionsFoundCounter;
    private double bestTotalSpeed;

    public HSNPBranchAndBound(List<Boat> boats, List<Sailor> sailors, boolean useImprovements) {
        this.console = new Console();
        this.boats = boats;
        this.sailors = sailors;
        this.useMarking = useImprovements;
        this.solutionsFoundCounter = 0;
        this.bestTotalSpeed = 0;
    }

    @Override
    public void run() {
        HSNPBranchAndBoundConfiguration bestConfiguration = this.branchAndBound();

        if (bestConfiguration != null) {
            this.console.showMessage("Solutions found: " + this.solutionsFoundCounter, true);
            this.console.spacing();
            this.console.showMessage("Best configuration: ", true);

            for (int i = 0; i < this.boats.size(); i++) {
                this.console.showMessage("Boat " + (i + 1) + ": " + boats.get(i).getName(), true);

                for (int j = 0; j < this.sailors.size(); j++) {
                    if (bestConfiguration.getSailorPosition(j) == i) {
                        this.console.showMessage("\tSailor " + sailors.get(j).getName(), true);
                    }
                }
            }

            this.console.spacing();
            this.console.showMessage("Total speed: " + this.value(bestConfiguration), true);
        } else {
            this.console.showError("There is not a solution", true);
        }
    }

    private HSNPBranchAndBoundConfiguration branchAndBound() {
        HSNPBranchAndBoundConfiguration configuration, bestConfiguration = null;
        HSNPBranchAndBoundConfiguration[] children;

        PriorityQueue<HSNPBranchAndBoundQueue> nodes = new PriorityQueue<>();

        configuration = (HSNPBranchAndBoundConfiguration) this.configureRoot();

        nodes.add(new HSNPBranchAndBoundQueue(configuration, this.sailors.size()));

        while (!nodes.isEmpty()) {
            configuration = nodes.poll().getConfiguration();

            children = (HSNPBranchAndBoundConfiguration[]) this.expand(configuration);

            for (HSNPBranchAndBoundConfiguration child : children) {
                if (this.solution(child)) {
                    if (this.useMarking) {
                        if (this.markedFeasible(child)) {
                            this.solutionsFoundCounter++;

                            if (this.markedValue(child) > this.bestTotalSpeed) {
                                this.bestTotalSpeed = this.markedValue(child);
                                bestConfiguration = new HSNPBranchAndBoundConfiguration(child, this.sailors.size());
                            }
                        }
                    } else {
                        if (this.feasible(child)) {
                            this.solutionsFoundCounter++;

                            if (this.value(child) > this.bestTotalSpeed) {
                                this.bestTotalSpeed = this.value(child);
                                bestConfiguration = new HSNPBranchAndBoundConfiguration(child, this.sailors.size());
                            }
                        }
                    }
                } else {
                    if (this.useMarking) {
                        if (this.markedCompletable(child)) {
                            if (this.markedPartialValue(child) > this.bestTotalSpeed) {
                                if (this.markedPartialValue(child) > this.bestTotalSpeed) {
                                    nodes.add(new HSNPBranchAndBoundQueue(child, this.markedEstimatedValue(child)));
                                }
                            }
                        }
                    } else {
                        if (this.completable(child)) {
                            if (this.partialValue(child) > this.bestTotalSpeed) {
                                nodes.add(new HSNPBranchAndBoundQueue(child, this.estimatedValue(child)));
                            }
                        }
                    }
                }
            }

        }

        return bestConfiguration;
    }

    @Override
    protected CommonConfiguration configureRoot() {
        return new HSNPBranchAndBoundConfiguration(HSNPBranchAndBound.SAILOR_ASSIGNED_NONE, this.sailors.size(), this.boats.size());
    }

    @Override
    protected CommonConfiguration[] expand(CommonConfiguration configuration) {
        CommonConfiguration[] children = new HSNPBranchAndBoundConfiguration[this.boats.size() + 1];

        for (int i = HSNPBranchAndBound.SAILOR_ASSIGNED_NONE; i < this.boats.size(); i++) {
            // Copy previous configuration
            HSNPBranchAndBoundConfiguration previous = (HSNPBranchAndBoundConfiguration) configuration;

            children[i + 1] = new HSNPBranchAndBoundConfiguration(previous.getK() + 1, this.sailors.size(), this.boats.size());
            HSNPBranchAndBoundConfiguration child = (HSNPBranchAndBoundConfiguration) children[i + 1];

            for (int j = 0; j <= configuration.getK(); j++) {
                child.setSailorPosition(j, previous.getSailorPosition(j));
            }

            // Create new child
            child.setSailorPosition(previous.getK() + 1, i);

            // Marking
            if (this.useMarking && i != HSNPBranchAndBound.SAILOR_ASSIGNED_NONE) {
                // Copy previous marking
                child.getMarking().setFullBoatsCounter(previous.getMarking().getFullBoatsCounter());
                child.getMarking().setTotalSpeed(previous.getMarking().getTotalSpeed());

                for (int y = 0; y < this.boats.size(); y++) {
                    child.getMarking().getSailorsByBoat()[y] = new HSNPBranchAndBoundBoatMarking(previous.getMarking().getSailorsByBoat()[y]);
                }

                child.getMarking().getSailorsByBoat()[i].setSailorsOnBoard(child.getMarking().getSailorsByBoat()[i].getSailorsOnBoard() + 1);
                child.getMarking().getSailorsByBoat()[i].setFull(this.boats.get(i).getCapacity() <=
                        child.getMarking().getSailorsByBoat()[i].getSailorsOnBoard());

                if (child.getMarking().getSailorsByBoat()[i].isFull() && this.boats.get(i).getCapacity() ==
                        child.getMarking().getSailorsByBoat()[i].getSailorsOnBoard()) {
                    child.getMarking().setFullBoatsCounter(child.getMarking().getFullBoatsCounter() + 1);
                }

                if (child.getMarking().getSailorsByBoat()[i].getSpeed() != 0) {
                    child.getMarking().setTotalSpeed(child.getMarking().getTotalSpeed() - child.getMarking().getSailorsByBoat()[i].getSpeed());
                }

                // Update total speed
                Boat boat = this.boats.get(i);

                double boatSpeed = 1;

                for (int j = child.getK(); j >= 0; j--) {
                    if (child.getSailors()[j] == i) {
                        boatSpeed *= this.sailors.get(j).getImpact(boat);
                    }
                }

                boatSpeed *= boat.getSpeed();

                child.getMarking().getSailorsByBoat()[i].setSpeed(boatSpeed);
                child.getMarking().setTotalSpeed(child.getMarking().getTotalSpeed() + boatSpeed);
            }
        }

        return children;
    }

    @Override
    protected boolean solution(CommonConfiguration configuration) {
        return configuration.getK() == (this.sailors.size() - 1);
    }

    @Override
    protected boolean completable(CommonConfiguration configuration) {
        int counter = 0;

        HSNPBranchAndBoundConfiguration config = (HSNPBranchAndBoundConfiguration) configuration;

        if (config.getSailorPosition(config.getK()) == HSNPBranchAndBound.SAILOR_ASSIGNED_NONE) {
            return true;
        }

        for (int i = 0; i <= config.getK(); i++) {
            if (config.getSailorPosition(i) == config.getSailorPosition(config.getK())) {
                counter++;
            }
        }

        return this.boats.get(config.getSailorPosition(config.getK())).getCapacity() >= counter;
    }

    @Override
    protected boolean feasible(CommonConfiguration configuration) {
        int[] sailorsByBoat = new int[this.boats.size()];

        Arrays.fill(sailorsByBoat, 0);

        HSNPBranchAndBoundConfiguration config = (HSNPBranchAndBoundConfiguration) configuration;

        for (int i = 0; i < this.sailors.size(); i++) {
            if (config.getSailorPosition(i) > HSNPBranchAndBound.SAILOR_ASSIGNED_NONE) {
                sailorsByBoat[config.getSailorPosition(i)]++;
            }
        }

        // Check that all boats are full
        for (int i = 0; i < this.boats.size(); i++) {
            if (this.boats.get(i).getCapacity() > sailorsByBoat[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected double value(CommonConfiguration configuration) {
        ArrayList<Sailor> sailorsByBoat = new ArrayList<>();
        double boatSpeed = 1;
        double totalSpeed = 0;

        HSNPBranchAndBoundConfiguration conf = (HSNPBranchAndBoundConfiguration) configuration;

        for (int i = 0; i < this.boats.size(); i++) {
            Boat ship = this.boats.get(i);

            for (int j = 0; j <= conf.getK(); j++) {
                if (conf.getSailorPosition(j) == i) {
                    sailorsByBoat.add(sailors.get(j));
                }
            }

            if (!sailorsByBoat.isEmpty()) {
                for (Sailor sailor : sailorsByBoat) {
                    boatSpeed *= sailor.getImpact(ship);
                }

                boatSpeed = ship.getSpeed() * boatSpeed;
                totalSpeed += boatSpeed;

                sailorsByBoat.clear();
            }
        }

        return totalSpeed;
    }

    @Override
    protected double partialValue(CommonConfiguration configuration) {
        return this.value(configuration);
    }

    @Override
    protected double estimatedValue(CommonConfiguration configuration) {
        return (this.sailors.size() - (configuration.getK() + 1)) / this.value(configuration);
    }

    private boolean markedCompletable(CommonConfiguration configuration) {
        HSNPBranchAndBoundConfiguration conf = (HSNPBranchAndBoundConfiguration) configuration;

        if (conf.getSailorPosition(conf.getK()) == HSNPBranchAndBound.SAILOR_ASSIGNED_NONE) {
            return true;
        }

        return this.boats.get(conf.getSailorPosition(conf.getK())).getCapacity() >=
                conf.getMarking().getSailorsByBoat()[conf.getSailorPosition(conf.getK())].getSailorsOnBoard();
    }

    private boolean markedFeasible(CommonConfiguration configuration) {
        HSNPBranchAndBoundConfiguration conf = (HSNPBranchAndBoundConfiguration) configuration;

        return conf.getMarking().getFullBoatsCounter() == this.boats.size();
    }

    private double markedPartialValue(CommonConfiguration configuration) {
        return this.markedValue(configuration);
    }


    private double markedValue(CommonConfiguration configuration) {
        HSNPBranchAndBoundConfiguration conf = (HSNPBranchAndBoundConfiguration) configuration;

        return conf.getMarking().getTotalSpeed();
    }

    private double markedEstimatedValue(CommonConfiguration configuration) {
        return (this.sailors.size() - (configuration.getK() + 1)) / this.markedValue(configuration);
    }
}
