package business.managers;

import business.models.Sailor;
import persistence.DAO.SailorsDAO;

import java.util.List;

public class SailorsManager {
    private final List<Sailor> sailors;

    public SailorsManager(String filePath) {
        this.sailors = new SailorsDAO(filePath).getSailors();
    }

    public List<Sailor> getSailors() {
        return sailors;
    }
}