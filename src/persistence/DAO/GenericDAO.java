package persistence.DAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class GenericDAO implements FileReader {
    private final String filePath;
    protected String fileText;

    public GenericDAO(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String readFile() {
        try {
            return new String(Files.readAllBytes(Path.of(this.filePath)));
        } catch (IOException e) {
            System.out.println("ERROR: Cannot read file \"" + this.filePath + "\"");
            return null;
        }
    }
}