package michaelarn0ld.mastery.data.abstractions;

import michaelarn0ld.mastery.models.Client;
import michaelarn0ld.mastery.models.State;

import java.util.List;

public abstract class ClientFileRepository<T extends Client> extends FileRepository<T> {

    public ClientFileRepository(String filePath) {
        super(filePath);
    }

    /**
     * Finds the Client with the matching email
     *
     * @param email - unique identifier
     * @return a Client with a unique, matching email
     */
    public T findByEmail(String email) {
        return findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds all Client with a matching State, if they exist
     *
     * @param s - State to match on
     * @return a List<Client> who have a matching State
     */
    public List<T> findByState(State s) {
        return findAll().stream()
                .filter(c -> c.getState() == s)
                .toList();
    }
}