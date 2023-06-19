package business.problems.high_speed_navigation.branch_and_bound;

import business.models.methods.branch_and_bound.CommonConfiguration;

import java.util.Arrays;

public class HSNPBranchAndBoundConfiguration extends CommonConfiguration {
    private int[] sailors;
    private HSNPBranchAndBoundMarking marking;

    public HSNPBranchAndBoundConfiguration(int k, int sailorsQty, int boatsQty) {
        super(k);
        this.sailors = new int[sailorsQty];
        this.marking = new HSNPBranchAndBoundMarking(boatsQty);
    }

    public HSNPBranchAndBoundConfiguration(HSNPBranchAndBoundConfiguration configuration, int sailorsQty) {
        super(configuration.getK());
        this.sailors = Arrays.copyOf(configuration.sailors, sailorsQty);
    }

    public int[] getSailors() {
        return sailors;
    }

    public int getSailorPosition(int position) {
        return this.sailors[position];
    }

    public void setSailorPosition(int position, int value) {
        this.sailors[position] = value;
    }

    public HSNPBranchAndBoundMarking getMarking() {
        return marking;
    }
}
