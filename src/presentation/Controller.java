package presentation;

import business.managers.BoatsManager;
import business.managers.SailorsManager;
import business.models.ProblemMethod;
import business.problems.ProblemController;
import business.problems.complete_fleet.CompleteFleetProblemController;
import business.problems.high_speed_navigation.HighSpeedNavigationProblemController;
import persistence.Paths;

public class Controller {
    private Console console;
    private BoatsManager boatsManager;
    private SailorsManager sailorsManager;
    private HighSpeedNavigationProblemController hsnpController;
    private CompleteFleetProblemController cfController;
    private long initialTime;
    private long finalTime;

    public Controller() {
        this.console = new Console();
    }

    public void run() {
        // Ask for dataset size
        String datasetSize = this.console.askDatasetSize();

        // Load datasets
        this.boatsManager = new BoatsManager(Paths.getBoatsDataset(datasetSize));
        this.sailorsManager = new SailorsManager(Paths.getSailorsDataset(datasetSize));

        // Ask for problem to solve
        int problemOption;

        do {
            problemOption = this.console.askForProblem();

            switch (problemOption) {
                case 1 -> this.runHighSpeedNavigationProblem();
                case 2 -> this.runCompleteFleetProblem();
                case 3 -> this.console.showMessage("Exiting...", true);
                default -> this.console.showError("Invalid option.", true);
            }
        } while (problemOption != 3);
    }

    private void runHighSpeedNavigationProblem() {
        this.hsnpController = new HighSpeedNavigationProblemController(this.boatsManager, this.sailorsManager);

        // Ask for a method to resolve the problem
        int methodOption = this.console.askForMethod();

        switch (methodOption) {
            case 1 -> this.selectImprovements(this.hsnpController, ProblemMethod.Backtracking);
            case 2 -> this.selectImprovements(this.hsnpController, ProblemMethod.BranchAndBound);
        }
    }

    private void runCompleteFleetProblem() {
        this.cfController = new CompleteFleetProblemController(this.boatsManager, this.sailorsManager);

        // Ask for a method to resolve the problem
        int methodOption = this.console.askForMethod();

        switch (methodOption) {
            case 1 -> this.selectImprovements(this.cfController, ProblemMethod.Backtracking);
            case 2 -> this.selectImprovements(this.cfController, ProblemMethod.BranchAndBound);
        }
    }

    private void selectImprovements(ProblemController problemController, ProblemMethod method) {
        // Ask for whether using or not performance improvements to resolve the problem
        int methodOption = this.console.askForImprovement();

        // Start timer
        this.startTimer();

        this.console.showMessage("Solving problem...", true);
        this.console.spacing();

        switch (methodOption) {
            case 1 -> {
                switch (method) {
                    case Backtracking -> problemController.solveUsingBacktracking(false);
                    case BranchAndBound -> problemController.solveUsingBranchAndBound(false);
                }
            }
            case 2 -> {
                switch (method) {
                    case Backtracking -> problemController.solveUsingBacktracking(true);
                    case BranchAndBound -> problemController.solveUsingBranchAndBound(true);
                }
            }
        }

        // Stop timer
        this.stopTimer();
    }

    private void startTimer() {
        this.initialTime = System.currentTimeMillis();
    }

    private void stopTimer() {
        this.finalTime = System.currentTimeMillis();

        this.console.showChronometer(this.finalTime - this.initialTime);
        this.console.spacing();
    }
}
