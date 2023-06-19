package business.problems.high_speed_navigation;

import business.problems.ProblemController;
import business.managers.BoatsManager;
import business.managers.SailorsManager;
import business.problems.high_speed_navigation.backtracking.HSNPBacktracking;
import business.problems.high_speed_navigation.branch_and_bound.HSNPBranchAndBound;

public class HighSpeedNavigationProblemController extends ProblemController {
    public HighSpeedNavigationProblemController(BoatsManager boatsManager, SailorsManager sailorsManager) {
        super(boatsManager, sailorsManager);
    }

    @Override
    public void solveUsingBacktracking(boolean useImprovements) {
        HSNPBacktracking hsnpBacktracking = new HSNPBacktracking(this.getBoatsManager().getBoats(),
                this.getSailorsManager().getSailors(), useImprovements);
        hsnpBacktracking.run();
    }

    @Override
    public void solveUsingBranchAndBound(boolean useImprovements) {
        HSNPBranchAndBound hsnpBranchAndBound = new HSNPBranchAndBound(this.getBoatsManager().getBoats(),
                this.getSailorsManager().getSailors(), useImprovements);
        hsnpBranchAndBound.run();
    }
}
