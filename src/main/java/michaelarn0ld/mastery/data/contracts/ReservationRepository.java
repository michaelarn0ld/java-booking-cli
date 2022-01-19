package michaelarn0ld.mastery.data.contracts;

import michaelarn0ld.mastery.data.exceptions.DataException;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHost(Host h);
    Reservation findById(Host h, int id);
    Reservation add(Reservation r) throws DataException;
    boolean update(Reservation r) throws DataException;
    boolean delete(Reservation r) throws DataException;
}
