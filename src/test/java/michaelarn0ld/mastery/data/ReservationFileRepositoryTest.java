package michaelarn0ld.mastery.data;

import michaelarn0ld.mastery.data.exceptions.DataException;
import michaelarn0ld.mastery.models.Guest;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    static final String SEED_PATH = "./data/reservations_test/reservation_seed.csv";
    static final String TEST_PATH = "./data/reservations_test/293508da-e367-437a-9178-1ebaa7d83015.csv";
    static final String DIR = "./data/reservations_test";
    static final String HOST_EMAIL = "nmclurg1s@umn.edu";
    static final String GUEST_EMAIL = "dfenelowd@google.nl";

    HostFileRepository hostRepo = new HostFileRepository("./data/hosts.csv");
    GuestFileRepository guestRepo = new GuestFileRepository("./data/guests.csv");
    ReservationFileRepository repo = new ReservationFileRepository(DIR);
    Host h = new Host();
    Guest g = new Guest();

    @BeforeEach
    void setup() throws IOException, DataException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
        h = hostRepo.findByEmail(HOST_EMAIL);
        g = guestRepo.findByEmail(GUEST_EMAIL);
    }

    @Test
    void shouldFindByHost() {
        List<Reservation> all = repo.findByHost(h);
        assertEquals(13, all.size());
    }

    @Test
    void shouldNotFindByBadHost() {
        Host badHost = new Host();
        badHost.setId("bad-id-123");
        List<Reservation> all = repo.findByHost(badHost);
        assertEquals(0, all.size());
    }

    @Test
    void shouldFindById() {
        Reservation seven = repo.findById(h, 7);
        assertEquals(seven.getHost().getLastName(), "McLurg");
        assertEquals(390, seven.getGuest().getId());
    }

    @Test
    void shouldNotFindBadId() {
        Reservation reservation = repo.findById(h, 12312);
        assertNull(reservation);
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation r = new Reservation();

        r.setCheckIn(LocalDate.parse("2021-10-10"));
        r.setCheckOut(LocalDate.parse("2021-10-17"));
        r.setHost(h);
        r.setGuest(g);

        Reservation actual = repo.add(r);
        assertEquals(14, repo.findByHost(h).size());
        assertEquals(14, repo.findByHost(h).get(13).getId());
        // 2*511.25 + 5*409 = 3067.50
        assertEquals(new BigDecimal("3067.50"), repo.findByHost(h).get(13).getTotal());
    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation r = repo.findById(h, 9);
        r.setCheckIn(LocalDate.parse("2030-12-31"));
        r.setCheckOut(LocalDate.parse("2031-02-10"));
        boolean actual = repo.update(r);
        assertTrue(actual);
        assertEquals(13, repo.findByHost(h).size());
        assertEquals(LocalDate.of(2030,12,31), repo.findById(h,9).getCheckIn());
    }

    @Test
    void shouldNotUpdateBadId() throws DataException {
        Reservation r = new Reservation();
        r.setId(99);
        r.setHost(h);
        r.setCheckIn(LocalDate.parse("2030-12-31"));
        r.setCheckOut(LocalDate.parse("2031-02-10"));
        boolean actual = repo.update(r);
        assertFalse(actual);
    }

    @Test
    void shouldDelete() throws DataException {
        Reservation r = repo.findById(h, 9);
        boolean actual = repo.delete(r);
        assertTrue(actual);
        assertEquals(12, repo.findByHost(h).size());
    }

    @Test
    void shouldNotDelete() throws DataException {
        Reservation r = new Reservation();
        r.setId(99);
        r.setHost(h);
        boolean actual = repo.delete(r);
        assertFalse(actual);
    }

}