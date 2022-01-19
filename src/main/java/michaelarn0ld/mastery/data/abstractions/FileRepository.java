package michaelarn0ld.mastery.data.abstractions;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileRepository<T> {

    protected int FIELDS;
    protected String filePath;

    public FileRepository(){};
    public FileRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Finds all serialized instances of T from the specified file path.
     *
     * @return all deserialized T instances parsed from the file
     */
    public List<T> findAll() {
        ArrayList<T> all = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // read header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == FIELDS) {
                    all.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
        }
        return all;
    }

    protected abstract T deserialize(String[] fields);
}