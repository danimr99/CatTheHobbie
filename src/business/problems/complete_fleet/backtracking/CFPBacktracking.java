package business.problems.complete_fleet.backtracking;

import business.models.Center;
import business.models.boats.Boat;
import business.models.boats.BoatType;
import business.models.methods.Backtracking;
import presentation.Console;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFPBacktracking extends Backtracking {
    private static final int CENTER_NOT_ASSIGNED = -1;
    private static final int CENTER_TAKEN = 1;
    private final Console console;
    private final List<Boat> boats;
    private final List<Center> centers;
    private final boolean useMarking;
    private final boolean usePbmsc;
    private int solutionsFoundCounter;
    private int[] bestConfiguration;
    private int minimumUsedCenters;


    public CFPBacktracking(List<Boat> boats, List<Center> centers, boolean useImprovements) {
        this.console = new Console();
        this.boats = boats;
        this.useMarking = useImprovements;
        this.usePbmsc = useImprovements;
        this.solutionsFoundCounter = 0;
        this.centers = centers;
        this.minimumUsedCenters = this.centers.size() + 1;
    }

    @Override
    public void run() {
        // Set up initial configuration
        int[] x = new int[this.centers.size()];
        int k = 0;

        if (this.useMarking) {
            CFPBacktrackingMarking m = new CFPBacktrackingMarking();

            this.backtracking(x, k, m);
        } else {
            this.backtracking(x, k, null);
        }

        if (this.bestConfiguration != null) {
            this.console.showMessage("Solutions found: " + this.solutionsFoundCounter, true);

            this.console.showMessage("Best solution: ", true);
            for (int i = 0; i < this.bestConfiguration.length; i++) {
                if (this.bestConfiguration[i] == 1) {
                    this.console.showMessage(" - " + centers.get(i).getName(), true);
                }
            }

            this.console.spacing();
            this.console.showMessage("Minimum centers required: " + this.minimumUsedCenters, true);
        } else {
            this.console.showError("There is not a solution", true);
        }
    }

    public void backtracking(int[] x, int k, CFPBacktrackingMarking m) {
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
                if (this.completable(x, k)) {
                    if (this.usePbmsc) {
                        if (m.getNumberOfCentersUsed() < this.minimumUsedCenters) {
                            this.backtracking(x, k + 1, m);
                        }
                    } else {
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
        x[k] = CFPBacktracking.CENTER_NOT_ASSIGNED;
    }

    @Override
    protected boolean existsSuccessor(int[] x, int k) {
        return x[k] < CFPBacktracking.CENTER_TAKEN;
    }

    @Override
    protected void existsNext(int[] x, int k) {
        x[k]++;
    }

    @Override
    protected boolean solution(int[] x, int k) {
        return k == (this.centers.size() - 1);
    }

    @Override
    protected boolean completable(int[] x, int k) {
        return true;
    }

    @Override
    protected boolean feasible(int[] x) {
        Map<BoatType, Integer> types = new HashMap<>();
        int counter = 0;

        types.put(BoatType.Windsurf, 0);
        types.put(BoatType.Optimist, 0);
        types.put(BoatType.Laser, 0);
        types.put(BoatType.PatiCatala, 0);
        types.put(BoatType.HobieDragoon, 0);
        types.put(BoatType.HobieCat, 0);

        for (int i = 0; i < this.centers.size(); i++) {
            if (x[i] == 1) {
                for (Boat boat : centers.get(i).getBoats()) {
                    if (types.get(boat.getType()) == 0) {
                        counter++;
                    }

                    types.put(boat.getType(), types.get(boat.getType()) + 1);
                }
            }
        }

        return counter == types.size();
    }

    @Override
    protected void treatSolution(int[] x) {
        int counter = 0;

        for (int i = 0; i < this.centers.size(); i++) {
            if (x[i] == CFPBacktracking.CENTER_TAKEN) {
                counter++;
            }
        }

        if (counter <= this.minimumUsedCenters) {
            this.minimumUsedCenters = counter;
            this.bestConfiguration = Arrays.copyOf(x, this.centers.size());
        }
    }

    private void mark(int[] x, int k, CFPBacktrackingMarking m) {
        if (x[k] == CENTER_TAKEN) {
            m.addUsedCenter();

            for (Boat boat : this.centers.get(k).getBoats()) {
                m.addType(boat.getType());

                if (m.getTypes().get(boat.getType()) == 1) {
                    m.addNumberOfTypes();
                }
            }
        }
    }

    private void unmark(int[] x, int k, CFPBacktrackingMarking m) {
        if (x[k] == CFPBacktracking.CENTER_TAKEN) {
            m.subtractUsedCenter();

            for (Boat boat : this.centers.get(k).getBoats()) {
                if (m.getTypes().get(boat.getType()) > 0) {
                    m.subtractType(boat.getType());
                }

                if (m.getTypes().get(boat.getType()) == 0) {
                    m.subtractNumberOfTypes();
                }
            }
        }
    }

    private boolean markedFeasible(CFPBacktrackingMarking m) {
        return m.getNumberOfTypes() == m.getTypes().size();
    }

    private void markedTreatSolution(int[] x, CFPBacktrackingMarking m) {
        if (m.getNumberOfCentersUsed() <= this.minimumUsedCenters) {
            this.minimumUsedCenters = m.getNumberOfCentersUsed();
            this.bestConfiguration = Arrays.copyOf(x, this.centers.size());
        }
    }
}
