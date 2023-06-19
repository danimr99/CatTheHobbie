package business.problems.complete_fleet.branch_and_bound;

import business.models.Center;
import business.models.boats.Boat;
import business.models.boats.BoatType;
import business.models.methods.branch_and_bound.BranchAndBound;
import business.models.methods.branch_and_bound.CommonConfiguration;
import presentation.Console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class CFPBranchAndBound extends BranchAndBound {
    private static final int CENTER_NOT_ASSIGNED = -1;
    private static final int CENTER_TAKEN = 1;
    private final Console console;
    private final List<Boat> boats;
    private final List<Center> centers;
    private final boolean useMarking;
    private int solutionsFoundCounter;
    private int minimumUsedCenters;

    public CFPBranchAndBound(List<Boat> boats, List<Center> centers, boolean useImprovements) {
        this.console = new Console();
        this.boats = boats;
        this.centers = centers;
        this.useMarking = useImprovements;
        this.solutionsFoundCounter = 0;
        this.minimumUsedCenters = this.centers.size() + 1;
    }

    @Override
    public void run() {
        CFPBranchAndBoundConfiguration bestConfiguration = this.branchAndBound();

        if (bestConfiguration != null) {
            this.console.showMessage("Solutions found: " + this.solutionsFoundCounter, true);

            this.console.showMessage("Best solution: ", true);
            for (int i = 0; i < this.centers.size(); i++) {
                if (bestConfiguration.getCenterPosition(i) == CFPBranchAndBound.CENTER_TAKEN) {
                    this.console.showMessage(" - " + centers.get(i).getName(), true);
                }
            }

            this.console.spacing();
            this.console.showMessage("Minimum centers required: " + this.minimumUsedCenters, true);
        } else {
            this.console.showError("There is not a solution", true);
        }
    }

    private CFPBranchAndBoundConfiguration branchAndBound() {
        CFPBranchAndBoundConfiguration configuration, bestConfiguration = null;
        CFPBranchAndBoundConfiguration[] children;

        PriorityQueue<CFPBranchAndBoundQueue> nodes = new PriorityQueue<>();

        configuration = (CFPBranchAndBoundConfiguration) this.configureRoot();

        nodes.add(new CFPBranchAndBoundQueue(configuration, this.centers.size()));

        while (!nodes.isEmpty()) {
            configuration = nodes.poll().getConfiguration();

            children = (CFPBranchAndBoundConfiguration[]) this.expand(configuration);

            for (CFPBranchAndBoundConfiguration child : children) {
                if (this.solution(child)) {
                    if (this.useMarking) {
                        if (this.feasible(child)) {
                            this.solutionsFoundCounter++;

                            if (this.markedValue(child) < this.minimumUsedCenters) {
                                this.minimumUsedCenters = this.markedValue(child);
                                bestConfiguration = new CFPBranchAndBoundConfiguration(child, this.centers.size());
                            }
                        }
                    } else {
                        if (this.feasible(child)) {
                            this.solutionsFoundCounter++;

                            if (this.value(child) < this.minimumUsedCenters) {
                                this.minimumUsedCenters = (int) this.value(child);
                                bestConfiguration = new CFPBranchAndBoundConfiguration(child, this.centers.size());
                            }
                        }
                    }
                } else {
                    if (this.useMarking) {
                        if (this.completable(child)) {
                            if (this.markedPartialValue(child) < this.minimumUsedCenters) {
                                nodes.add(new CFPBranchAndBoundQueue(child, this.markedEstimatedValue(child)));
                            }
                        }
                    } else {
                        if (this.completable(child)) {
                            if (this.partialValue(child) < this.minimumUsedCenters) {
                                nodes.add(new CFPBranchAndBoundQueue(child, this.estimatedValue(child)));
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
        return new CFPBranchAndBoundConfiguration(CFPBranchAndBound.CENTER_NOT_ASSIGNED, this.centers.size());
    }

    @Override
    protected CommonConfiguration[] expand(CommonConfiguration configuration) {
        CFPBranchAndBoundConfiguration[] children = new CFPBranchAndBoundConfiguration[2];

        for (int i = 0; i < 2; i++) {
            // Copy previous configuration
            CFPBranchAndBoundConfiguration previous = (CFPBranchAndBoundConfiguration) configuration;

            children[i] = new CFPBranchAndBoundConfiguration(previous.getK() + 1, this.centers.size());
            CFPBranchAndBoundConfiguration child = children[i];

            for (int j = 0; j <= configuration.getK(); j++) {
                child.setCenterPosition(j, previous.getCenterPosition(j));
            }

            // Set new position
            child.setCenterPosition(previous.getK() + 1, i);

            if (this.useMarking) {
                // Copy marking value
                child.getMarking().setNumberOfCentersUsed(previous.getMarking().getNumberOfCentersUsed());
                child.getMarking().setTypesUsed(new HashMap<>(previous.getMarking().getTypesUsed()));
                child.getMarking().setTypesUsedCounter(previous.getMarking().getTypesUsedCounter());

                // Marking
                child.getMarking().setNumberOfCentersUsed(child.getMarking().getNumberOfCentersUsed() + 1);

                // Update types used
                if (i == CFPBranchAndBound.CENTER_TAKEN) {
                    Center center = this.centers.get(previous.getK() + 1);

                    for (int j = 0; j < center.getBoats().size(); j++) {
                        child.getMarking().getTypesUsed().put(center.getBoats().get(j).getType(),
                                child.getMarking().getTypesUsed().get(center.getBoats().get(j).getType()) + 1);

                        if (child.getMarking().getTypesUsed().get(center.getBoats().get(j).getType()) ==
                                CFPBranchAndBound.CENTER_TAKEN) {
                            child.getMarking().setTypesUsedCounter(child.getMarking().getTypesUsedCounter() + 1);
                        }
                    }
                }
            }
        }

        return children;
    }

    @Override
    protected boolean solution(CommonConfiguration configuration) {
        return configuration.getK() == (this.centers.size() - 1);
    }

    @Override
    protected boolean completable(CommonConfiguration configuration) {
        return true;
    }

    @Override
    protected boolean feasible(CommonConfiguration configuration) {
        Map<BoatType, Integer> types = new HashMap<>();
        int counter = 0;

        types.put(BoatType.Windsurf, 0);
        types.put(BoatType.Optimist, 0);
        types.put(BoatType.Laser, 0);
        types.put(BoatType.PatiCatala, 0);
        types.put(BoatType.HobieDragoon, 0);
        types.put(BoatType.HobieCat, 0);

        CFPBranchAndBoundConfiguration conf = (CFPBranchAndBoundConfiguration) configuration;

        for (int i = 0; i < this.centers.size(); i++) {
            if (conf.getCenterPosition(i) == CFPBranchAndBound.CENTER_TAKEN) {
                for (Boat boat : centers.get(i).getBoats()) {
                    if (types.get(boat.getType()) == 0) {
                        counter++;
                    }

                    types.put(boat.getType(), types.get(boat.getType()) + 1);
                }
            }
        }

        return counter == (BoatType.values().length - 1);
    }

    @Override
    protected double value(CommonConfiguration configuration) {
        int counter = 0;

        CFPBranchAndBoundConfiguration conf = (CFPBranchAndBoundConfiguration) configuration;

        for (int i = 0; i <= conf.getK(); i++) {
            if (conf.getCenterPosition(i) == CFPBranchAndBound.CENTER_TAKEN) {
                counter++;
            }
        }

        return counter;
    }

    @Override
    protected double partialValue(CommonConfiguration configuration) {
        return this.value(configuration);
    }

    @Override
    protected double estimatedValue(CommonConfiguration configuration) {
        return (this.value(configuration) / (configuration.getK() + 1)) * (this.centers.size() - configuration.getK() + 1);
    }

    private int markedValue(CFPBranchAndBoundConfiguration configuration) {
        return configuration.getMarking().getNumberOfCentersUsed();
    }

    private double markedPartialValue(CFPBranchAndBoundConfiguration configuration) {
        return this.markedValue(configuration);
    }

    private double markedEstimatedValue(CFPBranchAndBoundConfiguration configuration) {
        return (double) (this.markedValue(configuration) / (configuration.getK() + 1)) *
                (this.centers.size() - configuration.getK() + 1);
    }
}
