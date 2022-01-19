package michaelarn0ld.mastery.ui;

import michaelarn0ld.mastery.models.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class View {

    private static final String VALID_EMAIL_ADDRESS = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    /**
     * Show the user menu options and allow them to select a valid choice
     *
     * @return the users choice
     */
    public int chooseMenuOption() {
        displayHeader("Main Menu");
        io.println("0. Exit");
        io.println("1. View Reservations by Host");
        io.println("2. Add a Reservation");
        io.println("3. Update a Reservation");
        io.println("4. Delete a Reservation");
        Integer result;
        do {
            result = io.readInt("Choose [0-4]: ", 0, 4);
        } while (result == null);
        return result;
    }

    /**
     * Returns a user input email address
     *
     * @param prompt - ask the user what kind of email to enter
     * @return a regex validated email
     */
    public String getEmail(String prompt) {
        String email;
        do {
            email = io.readString(prompt);
            if (!email.toUpperCase().matches(VALID_EMAIL_ADDRESS)) {
               io.printf("\n[ERR]\nINPUT MUST BE A VALID EMAIL\n");
            }
        } while (!email.toUpperCase().matches(VALID_EMAIL_ADDRESS));
        return email;
    }

    /**
     * Creates a Reservation
     *
     * @param h - Host for the reservation
     * @param g - Guest for the reservation
     * @return - a Reservation to be validated
     */
    public Reservation createReservation(Host h, Guest g) {
        Reservation r = new Reservation();
        LocalDate checkIn = io.readDate("Check In (MM/dd/yyyy): ");
        LocalDate checkOut = io.readDate("Check Out (MM/dd/yyyy): ");
        r.setHost(h);
        r.setGuest(g);
        r.setCheckIn(checkIn);
        r.setCheckOut(checkOut);
        return r;
    }

    /**
     * Updates the data of a Reservation
     *
     * @param r - Reservation to be updated
     * @return an updated Reservation
     */
    public Reservation updateReservation(Reservation r) {
        LocalDate checkIn = io.readDate("Check In (" +
                r.getCheckIn().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + "): ");
        if (checkIn != null) {
            r.setCheckIn(checkIn);
        }
        LocalDate checkOut = io.readDate("Check Out (" +
                r.getCheckOut().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + "): ");
        if (checkOut != null) {
            r.setCheckOut(checkOut);
        }
        return r;
    }

    /**
     * Gets a State from the user
     *
     * @param prompt - ask the user for a valid State.
     * @return a State
     */
    public State getState(String prompt) {
        return io.readState(prompt);
    }

    /**
     * Gets Reservation ID from the user
     *
     * @param prompt - ask the user for a valid Reservation ID
     * @param reservations - reservations to draw the id from
     * @return a Reservation id
     */
    public int getReservationId(String prompt, List<Reservation> reservations) {
        Integer id;
        do {
            id = io.readInt(prompt);
            if (id == null) {
                displayMessage("[ERR]\nINPUT MUST BE A VALID RESERVATION ID\n");
                continue;
            }
            int finalId = id;
            if (reservations.stream()
                    .map(Reservation::getId)
                    .anyMatch(i -> i == finalId)) {
                return id;
            }
            displayMessage("[ERR]\nINPUT MUST BE A VALID RESERVATION ID\n");
        } while (true);
    }

    /**
     * Determine if the user wants to list Client based on State
     *
     * @param prompt - ask the user if they want to filter on State
     * @return true if they want to filter on State
     */
    public boolean getFilterOnPrompt(String prompt) {
        return io.readBoolean(prompt);
    }

    /**
     * Determines if the user wants to confirm the Reservation before they add it
     * to the repository
     *
     * @param r - Reservation to be confirmed
     * @return true if the user confirms the information is correct
     */
    public boolean confirmReservation(Reservation r) {
        displayHeader("Summary");
        io.printf("Start: %s\nEnd: %s\nTotal: $%s\n",
                r.getCheckIn(),
                r.getCheckOut(),
                r.getTotal());
        return io.readBoolean("Is this ok? [y/n]: ");
    }

    /**
     * Asks if the user wants to delete a Reservation
     *
     * @param r - Reservation to be deleted
     * @return true if the user confirms they want to delete the Reservation
     */
    public boolean deleteReservation(Reservation r) {
        displayHeader("Remove Reservation");
        io.printf("(%s, %s)\nHost: %s\nGuest: %s %s\nCheck In: %s\nCheck Out: %s\n",
                r.getHost().getCity(),
                r.getHost().getState(),
                r.getHost().getLastName(),
                r.getGuest().getFirstName(),
                r.getGuest().getLastName(),
                r.getCheckIn(),
                r.getCheckOut());
        return io.readBoolean("\nAre you sure you want to remove this reservation? [y/n]: ");
    }

    /**
     * Prints a formatted list of Reservation data
     *
     * @param reservations - List<Reservation> to print
     */
    public void showReservations(List<Reservation> reservations) {
        displayHeader("RESERVATION ID    CHECK IN      CHECK OUT     GUEST ID    TOTAL");
        reservations.stream()
                .sorted(Comparator.comparing(Reservation::getCheckIn))
                .forEach(
                r -> io.printf("%-14s    %-10s    %-10s    %-8s    %s\n",
                     r.getId(),
                     r.getCheckIn(),
                     r.getCheckOut(),
                     r.getGuest().getId(),
                     r.getTotal()
        ));
    }

    /**
     * Prints a formatted list of <T extends Client> data
     *
     * @param clients - List<T extends Client> to print
     */
    public <T extends Client> void showClients(List<T> clients) {
        displayHeader("LAST NAME               EMAIL                                 STATE");
        clients.forEach(
                c -> io.printf("%-20s    %-34s    %s\n",
                        c.getLastName(),
                        c.getEmail(),
                        c.getState())
        );
        io.println("");
    }

    /**
     * Display an underlined header
     * @param message - the header shown to the user
     */
    public void displayHeader(String message) {
        int length = message.length();
        io.println("");
        io.println(message);
        io.println("=".repeat(length));
    }

    /**
     * Display a formatted message
     *
     * @param format - String format
     * @param values - replacements for the formatted string
     */
    public void displayMessage(String format, Object... values) {
        io.printf(format, values);
    }

    /**
     * Display all errors received from the given result
     *
     * @param errors - errors as a list from Result<T>.getErrors()
     */
    public void displayErrors(List<String> errors) {
        displayHeader("Errors:");
        errors.forEach(io::println);
    }
}