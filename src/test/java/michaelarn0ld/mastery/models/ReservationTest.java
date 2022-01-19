package michaelarn0ld.mastery.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void shouldGetTotalWithWeekends() {
        Host host = new Host();
        host.setStandardRate(new BigDecimal("5.50"));
        host.setWeekendRate(new BigDecimal("15.82"));

        Reservation reservation = new Reservation();
        reservation.setCheckIn(LocalDate.of(2021, 12, 5));
        reservation.setCheckOut(LocalDate.of(2021, 12, 12));
        reservation.setHost(host);

        /*
        Should be...
        Total = 2*(15.82) + 5*(5.50) = 59.14
         */
        assertEquals(new BigDecimal("59.14"), reservation.getTotal());
    }

    @Test
    void shouldGetTotalWithOnlyWeek() {
        Host host = new Host();
        host.setStandardRate(new BigDecimal("19.99"));
        host.setWeekendRate(new BigDecimal("49.95"));

        Reservation reservation = new Reservation();
        reservation.setCheckIn(LocalDate.of(2021, 12, 6));
        reservation.setCheckOut(LocalDate.of(2021, 12, 11));
        reservation.setHost(host);

        /*
        Should be...
        Total = 5*(19.99) = 99.95
         */
        assertEquals(new BigDecimal("99.95"), reservation.getTotal());
    }

    @Test
    void shouldGetTotalWithYearChange() {
        Host host = new Host();
        host.setStandardRate(new BigDecimal("89.95"));
        host.setWeekendRate(new BigDecimal("120.99"));

        Reservation reservation = new Reservation();
        reservation.setCheckIn(LocalDate.of(2021, 12, 21));
        reservation.setCheckOut(LocalDate.of(2022, 1, 7));
        reservation.setHost(host);

        /*
        Should be...
        Total = 13*(89.95) +  4*(120.99) = 1653.31
         */
        assertEquals(new BigDecimal("1653.31"), reservation.getTotal());
    }
}