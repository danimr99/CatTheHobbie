package business.problems.complete_fleet;

import business.problems.ProblemController;
import business.managers.BoatsManager;
import business.managers.SailorsManager;
import business.problems.complete_fleet.backtracking.CFPBacktracking;
import business.problems.complete_fleet.branch_and_bound.CFPBranchAndBound;

public class CompleteFleetProblemController extends ProblemController {
    public CompleteFleetProblemController(BoatsManager boatsManager, SailorsManager sailorsManager) {
        super(boatsManager, sailorsManager);
    }

    @Override
    public void solveUsingBacktracking(boolean useImprovements) {
        CFPBacktracking cfpBacktracking = new CFPBacktracking(this.getBoatsManager().getBoats(),
                this.getBoatsManager().getCenters(), useImprovements);
        cfpBacktracking.run();
    }

    @Override
    public void solveUsingBranchAndBound(boolean useImprovements) {
        CFPBranchAndBound cfpBranchAndBound = new CFPBranchAndBound(this.getBoatsManager().getBoats(),
                this.getBoatsManager().getCenters(), useImprovements);
        cfpBranchAndBound.run();
    }
}
