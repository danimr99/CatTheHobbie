package business.models;

import business.models.boats.Boat;

import java.util.ArrayList;
import java.util.List;

public class Center {
    private String name;
    private List<Boat> boats;

    public Center(String name) {
        this.name = name;
        this.boats = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Boat> getBoats() {
        return boats;
    }

    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }

    public void addBoat(Boat boat) {
        this.boats.add(boat);
    }
}
