package michaelarn0ld.mastery.data;

import michaelarn0ld.mastery.data.contracts.ReservationRepository;
import michaelarn0ld.mastery.data.exceptions.DataException;
import michaelarn0ld.mastery.models.Guest;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.Reservation;
import michaelarn0ld.mastery.models.State;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {
        // set up host
        Host h = new Host();
        h.setId("test-id-1");
        h.setLastName("Bastrop");
        h.setEmail("ebastrop@wework.io");
        h.setPhoneNumber("(444) 4444444");
        h.setStreetAddress("123 Wonder St.");
        h.setCity("Corvallis");
        h.setState(State.OR);
        h.setPostalCode("97333");
        h.setStandardRate(BigDecimal.valueOf(120.99));
        h.setWeekendRate(BigDecimal.valueOf(189.95));

        // two guests
        Guest g1 = new Guest();
        g1.setId(1);

        Guest g2 = new Guest();
        g2.setId(2);

        // reservations
        Reservation r1 = new Reservation();
        r1.setId(1);
        r1.setHost(h);
        r1.setGuest(g1);
        r1.setCheckIn(LocalDate.parse("2022-10-18"));
        r1.setCheckOut(LocalDate.parse("2022-10-25"));
        reservations.add(r1);

        Reservation r2 = new Reservation();
        r2.setId(2);
        r2.setHost(h);
        r2.setGuest(g2);
        r2.setCheckIn(LocalDate.parse("2021-11-18"));
        r2.setCheckOut(LocalDate.parse("2021-11-25"));
        reservations.add(r2);
    }

    @Override
    public List<Reservation> findByHost(Host h) {
        return reservations.stream()
                .filter(r -> r.getHost().getId().equals(h.getId()))
                .toList();
    }

    @Override
    public Reservation findById(Host h, int id) {
        List<Reservation> hostReservations = reservations.stream()
                .filter(r -> r.getHost().getId().equals(h.getId()))
                .toList();
        return hostReservations.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Reservation add(Reservation r) throws DataException {
        return r;
    }

    @Override
    public boolean update(Reservation r) throws DataException {
        return findByHost(r.getHost()).stream()
                .anyMatch(res -> res.getId() == r.getId());
    }

    @Override
    public boolean delete(Reservation r) throws DataException {
        return findByHost(r.getHost()).stream()
                .anyMatch(res -> res.getId() == r.getId());
    }
}
