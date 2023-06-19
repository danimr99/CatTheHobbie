package business.problems.complete_fleet.branch_and_bound;

import business.models.methods.branch_and_bound.CommonConfiguration;

import java.util.Arrays;

public class CFPBranchAndBoundConfiguration extends CommonConfiguration {
    private int[] centers;
    private CFPBranchAndBoundMarking marking;

    public CFPBranchAndBoundConfiguration(int k, int centersQty) {
        super(k);
        this.centers = new int[centersQty];
        this.marking = new CFPBranchAndBoundMarking();
    }

    public CFPBranchAndBoundConfiguration(CFPBranchAndBoundConfiguration configuration, int centersQty) {
        super(configuration.getK());
        this.centers = Arrays.copyOf(configuration.centers, centersQty);
    }

    public CFPBranchAndBoundMarking getMarking() {
        return marking;
    }

    public void setCenterPosition(int position, int value) {
        this.centers[position] = value;
    }

    public int getCenterPosition(int position) {
        return this.centers[position];
    }
}
