package michaelarn0ld.mastery.domain;

import michaelarn0ld.mastery.data.*;
import michaelarn0ld.mastery.data.exceptions.DataException;
import michaelarn0ld.mastery.models.Guest;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.Reservation;
import michaelarn0ld.mastery.models.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    Host h = new Host();

    ReservationService service = new ReservationService(
            new ReservationRepositoryDouble(),
            new GuestRepositoryDouble(),
            new HostRepositoryDouble()
    );

    @BeforeEach
    void setUpHost() {
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
    }

    @Test
    void shouldFindByHost() {
        List<Reservation> actual = service.findByHost(h);
        assertEquals(2, actual.size());
    }

    @Test
    void shouldNotFindByWrongHost() {
        List<Reservation> actual = service.findByHost(new Host());
        assertEquals(0, actual.size());
    }

    @Test
    void shouldMapGuestsWhenFindByHost() {
        List<Reservation> actual = service.findByHost(h);
        assertEquals("Bob", actual.get(0).getGuest().getFirstName());
        assertEquals("Adi", actual.get(1).getGuest().getFirstName());
    }

    @Test
    void shouldFindRightId() {
        Reservation actual = service.findById(h, 2);
        assertEquals("ahoz23@gmail.com", actual.getGuest().getEmail());
    }

    @Test
    void shouldNotFindWrongId() {
        Reservation actual = service.findById(h, 12);
        assertNull(actual);
    }

    @Test
    void shouldNotAddNullReservation() throws DataException {
        Reservation r3 = null;
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.RESERVATION_NULL_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldNotAddNullCheckInReservation() throws DataException {
        Reservation r3 = new Reservation();
        r3.setGuest(new Guest());
        r3.setHost(h);
        r3.setCheckOut(LocalDate.of(2021,10,10));
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.CHECKIN_NULL_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldNotAddNullCheckOutReservation() throws DataException {
        Reservation r3 = new Reservation();
        r3.setGuest(new Guest());
        r3.setHost(h);
        r3.setCheckIn(LocalDate.of(2021,10,10));
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.CHECKOUT_NULL_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldNotAddNullGuestReservation() throws DataException {
        Reservation r3 = new Reservation();
        r3.setHost(h);
        r3.setCheckIn(LocalDate.of(2021,10,10));
        r3.setCheckOut(LocalDate.of(2021,10,10));
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.GUEST_NULL_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldNotAddNullHostReservation() throws DataException {
        Reservation r3 = new Reservation();
        r3.setGuest(new Guest());
        r3.setCheckIn(LocalDate.of(2021,10,10));
        r3.setCheckOut(LocalDate.of(2021,10,10));
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.HOST_NULL_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldNotAddOverlappingReservation() throws DataException {
        Reservation r3 = new Reservation();
        r3.setGuest(new Guest());
        r3.setHost(h);
        r3.setCheckIn(LocalDate.of(2022,10,19));
        r3.setCheckOut(LocalDate.of(2022,10,21));
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.OVERLAPPING_RESERVATION_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldNotAddReservationWithCheckoutBeforeCheckIn() throws DataException {
        Reservation r3 = new Reservation();
        r3.setGuest(new Guest());
        r3.setHost(h);
        r3.setCheckIn(LocalDate.of(2022,10,26));
        r3.setCheckOut(LocalDate.of(2022,10,22));
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.CHECKIN_AFTER_CHECKOUT_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldNotAddReservationWithCheckInInPast() throws DataException {
        Reservation r3 = new Reservation();
        r3.setGuest(new Guest());
        r3.setHost(h);
        r3.setCheckIn(LocalDate.of(2021,10,26));
        r3.setCheckOut(LocalDate.of(2021,10,27));
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.CHECKIN_IN_PAST_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldNotAddReservationWithBadGuest() throws DataException {
        Reservation r3 = new Reservation();
        r3.setGuest(new Guest());
        r3.setHost(h);
        r3.setCheckIn(LocalDate.of(2022,10,26));
        r3.setCheckOut(LocalDate.of(2022,10,29));
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.GUEST_NOT_FOUND_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldNotAddReservationWithBadHost() throws DataException {
        Reservation r3 = new Reservation();
        Guest g = new Guest();
        g.setId(2);
        r3.setGuest(g);
        h.setEmail("bademail@example.com");
        r3.setHost(h);
        r3.setCheckIn(LocalDate.of(2022,10,26));
        r3.setCheckOut(LocalDate.of(2022,10,29));
        Result<Reservation> result = service.add(r3);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.HOST_NOT_FOUND_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldAddReservation() throws DataException {
        Reservation r3 = new Reservation();
        Guest g = new Guest();
        g.setId(1);
        r3.setGuest(g);
        r3.setHost(h);
        r3.setCheckIn(LocalDate.of(2022,10,26));
        r3.setCheckOut(LocalDate.of(2022,10,29));
        Result<Reservation> result = service.add(r3);
        assertEquals(0, result.getErrors().size());
    }

    @Test
    void shouldNotUpdateInvalidId() throws DataException {
        Reservation r = new Reservation();
        r.setCheckIn(LocalDate.parse("2023-01-01"));
        r.setCheckOut(LocalDate.parse("2024-01-01"));
        r.setHost(h);
        Guest g = new Guest();
        g.setId(1);
        r.setGuest(g);
        r.setId(11);
        Result<Reservation> result = service.update(r);
        assertEquals(1, result.getErrors().size());
        assertEquals(ReservationService.RESERVATION_NOT_FOUND_ERROR, result.getErrors().get(0));
    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation r = service.findById(h, 1);
        r.setCheckOut(r.getCheckOut().plusWeeks(1));
        Result<Reservation> result = service.update(r);
        assertEquals(0, result.getErrors().size());
        assertEquals(new BigDecimal("1969.70"), result.getPayload().getTotal());
    }

    @Test
    void shouldNotDeleteInvalidReservation() throws DataException {
        Reservation r = new Reservation();
        r.setId(11);
        r.setHost(h);
        Result<Reservation> actual = service.delete(r);
        assertEquals(1, actual.getErrors().size());
        assertEquals(ReservationService.RESERVATION_NOT_FOUND_ERROR, actual.getErrors().get(0));
    }

    @Test
    void shouldDeleteReservation() throws DataException {
        Reservation r = service.findById(h, 1);
        Result<Reservation> actual = service.delete(r);
        assertEquals(0, actual.getErrors().size());
    }

}