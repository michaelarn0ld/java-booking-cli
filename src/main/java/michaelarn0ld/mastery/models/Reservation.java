package michaelarn0ld.mastery.models;

import michaelarn0ld.mastery.domain.ReservationService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {

    /*
    FIELDS
     */
    private int id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Host host;
    private Guest guest;

    /*
    GETTERS & SETTERS
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    /**
     * Determine the total cost of a reservation, taking into consideration the
     * differing rates between weekdays and weekends.
     *
     * @return BigDecimal value representing the total cost of the guest's stay
     */
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        LocalDate checkIn = this.checkIn;
        while (checkIn.compareTo(checkOut) < 0) {
            total = switch (checkIn.getDayOfWeek()) {
                case SATURDAY, SUNDAY -> total.add(host.getWeekendRate());
                default -> total.add(host.getStandardRate());
            };
            checkIn = checkIn.plusDays(1);
        }
        return total;
    }
}
