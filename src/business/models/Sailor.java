package business.models;

import business.models.boats.Boat;
import business.models.boats.BoatType;

import java.util.HashMap;

public class Sailor {

    private int numMembership;
    private String name;
    private double weight;
    private HashMap<BoatType, Integer> skills;
    private int winRatio;

    public Sailor(int numMembership, String name, double weight, HashMap<BoatType, Integer> skills, int winRatio) {
        this.numMembership = numMembership;
        this.name = name;
        this.weight = weight;
        this.skills = skills;
        this.winRatio = winRatio;
    }

    public double getImpact(Boat boat) {
        return (getWeightImpact(boat) + getSkillImpact(boat)) / 2;
    }

    private float getWeightImpact(Boat boat) {
        return (float) ((100 - this.weight) / boat.getWeight());
    }

    private double getSkillImpact(Boat boat) {
        return (double)(this.skills.get(boat.getType()) / 10) + (double)(this.winRatio / 100) / 2;
    }

    public String getName() {
        return name;
    }
}
