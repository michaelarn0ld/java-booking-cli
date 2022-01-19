package michaelarn0ld.mastery.data.contracts;

import michaelarn0ld.mastery.models.State;

import java.util.List;

public interface ClientRepository<T> {
    List<T> findAll();
    List<T> findByState(State s);
    T findByEmail(String email);
}
