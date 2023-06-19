package business.problems;

import business.managers.BoatsManager;
import business.managers.SailorsManager;
import presentation.Console;

public abstract class ProblemController {
    private final Console console;
    private final BoatsManager boatsManager;
    private final SailorsManager sailorsManager;

    public ProblemController(BoatsManager boatsManager, SailorsManager sailorsManager) {
        this.console = new Console();
        this.boatsManager = boatsManager;
        this.sailorsManager = sailorsManager;
    }

    public abstract void solveUsingBacktracking(boolean useImprovements);
    public abstract void solveUsingBranchAndBound(boolean useImprovements);

    public BoatsManager getBoatsManager() {
        return boatsManager;
    }

    public SailorsManager getSailorsManager() {
        return sailorsManager;
    }
}
