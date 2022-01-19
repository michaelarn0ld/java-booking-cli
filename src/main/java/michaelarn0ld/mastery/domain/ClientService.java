package michaelarn0ld.mastery.domain;

import michaelarn0ld.mastery.data.contracts.ClientRepository;
import michaelarn0ld.mastery.models.Client;
import michaelarn0ld.mastery.models.State;

import java.util.List;

public class ClientService<T extends Client> {

    private final ClientRepository<T> repository;

    public ClientService(ClientRepository<T> repository) {
        this.repository = repository;
    }

    /**
     * Fetches all T from the repository; ignores the header of the file
     *
     * @return a List<T> of all T from the repository
     */
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Fetches all T from the repository with a matching State
     *
     * @param s - State to match on
     * @return a List<T> that match on State from the repository
     */
    public List<T> findByState(State s) {
        return repository.findByState(s);
    }

    /**
     * Fetch a T with a matching email from the repository
     *
     * @param email - unique identifier for T
     * @return an instance of T, matching on email, from the repository
     */
    public T findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
