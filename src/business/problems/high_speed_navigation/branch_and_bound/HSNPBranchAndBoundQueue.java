package business.problems.high_speed_navigation.branch_and_bound;

public class HSNPBranchAndBoundQueue implements Comparable<HSNPBranchAndBoundQueue>{
    private HSNPBranchAndBoundConfiguration configuration;
    private double value;

    public HSNPBranchAndBoundQueue(HSNPBranchAndBoundConfiguration configuration, double value) {
        this.configuration = configuration;
        this.value = value;
    }

    public HSNPBranchAndBoundConfiguration getConfiguration() {
        return configuration;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(HSNPBranchAndBoundQueue o) {
        return Double.compare(this.value, o.getValue());
    }
}
