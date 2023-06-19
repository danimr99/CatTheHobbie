package business.problems.complete_fleet.branch_and_bound;

public class CFPBranchAndBoundQueue implements Comparable<CFPBranchAndBoundQueue> {
    private CFPBranchAndBoundConfiguration configuration;
    private double value;

    public CFPBranchAndBoundQueue(CFPBranchAndBoundConfiguration configuration, double value) {
        this.configuration = configuration;
        this.value = value;
    }

    public CFPBranchAndBoundConfiguration getConfiguration() {
        return configuration;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(CFPBranchAndBoundQueue o) {
        return Double.compare(this.getValue(), o.getValue());
    }
}
