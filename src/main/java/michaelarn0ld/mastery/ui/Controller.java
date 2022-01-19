package michaelarn0ld.mastery.ui;

import michaelarn0ld.mastery.data.exceptions.DataException;
import michaelarn0ld.mastery.domain.ClientService;
import michaelarn0ld.mastery.domain.ReservationService;
import michaelarn0ld.mastery.domain.Result;
import michaelarn0ld.mastery.models.*;

import java.time.LocalDate;
import java.util.List;

public class Controller {

    private final View view;
    private final ReservationService reservationService;
    private final ClientService<Guest> guestService;
    private final ClientService<Host> hostService;

    public Controller(View view,
                      ReservationService reservationService,
                      ClientService<Guest> guestService,
                      ClientService<Host> hostService) {
        this.view = view;
        this.reservationService = reservationService;
        this.guestService = guestService;
        this.hostService = hostService;
    }

    /**
     * Run the application unless there is a problem reading the data from the file repository
     */
    public void run() {
        try {
            runApp();
        } catch (DataException ex) {
            view.displayMessage("\nFATAL ERROR\nSHUTTING DOWN\n");
        }
    }

    /**
     * Read the option choice from the user. While it is not 0 the application will continue to run. When the user
     * selects 0, runApp() ends which subsequently terminates run()
     */
    private void runApp() throws DataException {
        for (int option = view.chooseMenuOption(); option > 0; option = view.chooseMenuOption()) {
            switch (option) {
                case 1:
                    viewReservationsByHost();
                    break;
                case 2:
                    addReservation();
                    break;
                case 3:
                    updateReservation();
                    break;
                case 4:
                    deleteReservation();
                    break;
            }
        }
    }

    /**
     * Shows Reservation instances based on Host; lets user know if the Host does not
     * exist or if there are no associated Reservation
     */
    private void viewReservationsByHost() {
        view.displayHeader("Find Reservations by Host");
        Host h = getClient(hostService, "Host");
        if (h != null) {
            List<Reservation> reservations = reservationService.findByHost(h);
            if (reservations.size() > 0) {
                view.showReservations(reservations);
            } else {
                view.displayMessage("\n[ERR]\nHOST HAS NO RESERVATIONS\n");
            }
        } else{
            view.displayMessage("\n[ERR]\nHOST NOT FOUND\n");
        }
    }

    /**
     * Adds a Reservation to the correct Host file, if the Reservation is valid
     * and confirmed by the user.
     */
    private void addReservation() throws DataException {
        view.displayHeader("Create a New Reservation");
        Host h = getClient(hostService, "Host");
        Guest g = getClient(guestService, "Guest");
        if (h == null || g == null) {
            view.displayMessage("\n[ERR]\nHOST OR GUEST NOT FOUND\n");
            return;
        }
        Reservation r;
        boolean confirmed;
        do {
            r = view.createReservation(h, g);
            confirmed = view.confirmReservation(r);
        } while (!confirmed);
        showResultMessage(reservationService.add(r), "create");
    }

    /**
     * Updates an existing Reservation to the correct Host file if the Reservation is valid and
     * confirmed by the user.
     */
    private void updateReservation() throws DataException {
        view.displayHeader("Update Reservations");
        Host h = getClient(hostService, "Host");
        if (h != null) {
            List<Reservation> reservations = getCurrentReservations(reservationService.findByHost(h));
            if (reservations.size() > 0) {
                view.displayHeader(String.format("%s: %s, %s", h.getLastName(), h.getCity(), h.getState()));
                reservations = filterReservationByGuest(reservations);
                view.showReservations(reservations);
                Reservation r = reservationService.findById(h,
                        view.getReservationId("Enter Reservation ID: ", reservations));
                if (r != null) {
                    boolean confirmed;
                    do {
                        r = view.updateReservation(r);
                        confirmed = view.confirmReservation(r);
                    } while (!confirmed);
                    showResultMessage(reservationService.update(r), "update");
                } else {
                    view.displayMessage("\n[ERR]\nRESERVATION NOT FOUND\n");
                }
            } else {
                view.displayMessage("\n[ERR]\nHOST HAS NO RESERVATIONS\n");
            }
        } else {
            view.displayMessage("\n[ERR]\nHOST NOT FOUND\n");
        }
    }

    /**
     * Deletes the Reservation from the proper Host file if it is valid and confirmed by the user
     */
    private void deleteReservation() throws DataException {
        view.displayHeader("Update Reservations");
        Host h = getClient(hostService, "Host");
        if (h != null) {
            List<Reservation> reservations = getCurrentReservations(reservationService.findByHost(h));
            if (reservations.size() > 0) {
                view.displayHeader(String.format("%s: %s, %s", h.getLastName(), h.getCity(), h.getState()));
                reservations = filterReservationByGuest(reservations);
                view.showReservations(reservations);
                Reservation r = reservationService.findById(h,
                        view.getReservationId("Enter Reservation ID: ", reservations));
                if (r != null) {
                    if (view.deleteReservation(r)) {
                        showResultMessage(reservationService.delete(r), "delete");
                    }
                } else {
                    view.displayMessage("\n[ERR]\nRESERVATION NOT FOUND\n");
                }
            } else {
                view.displayMessage("\n[ERR]\nHOST HAS NO RESERVATIONS\n");
            }
        } else {
            view.displayMessage("\n[ERR]\nHOST NOT FOUND\n");
        }
    }

    /**
     * Asks the users if they want to filter Reservation by Guest
     *
     * @param reservations - List<Reservation> to be filtered
     * @return a List<Reservation> of filtered Reservation
     */
    private List<Reservation> filterReservationByGuest(List<Reservation> reservations) {
        if (view.getFilterOnPrompt("Filter Reservations by Guest? ")) {
            Guest g;
            do {
                g = getClient(guestService, "Guest");
                if (g == null) {
                    view.displayMessage("\n[ERR]\nGUEST NOT FOUND\n");
                    continue;
                }
                Guest finalG = g; // need this for the lambda? Maybe a better solution
                if (reservations.stream().noneMatch(r -> r.getGuest().getId() == finalG.getId())) {
                    view.displayMessage("\n[ERR]\nNO CURRENT OR FUTURE RESERVATIONS FOR GUEST\n");
                    continue;
                }
                break;
            } while (true);
            Guest finalG1 = g; // need this for the lambda? Maybe a better solution
            reservations = reservations.stream()
                    .filter(r -> r.getGuest().getId() == finalG1.getId())
                    .toList();
        }
        return reservations;
    }

    /**
     * Displays the success or failure of a CRUD operation to the user.
     *
     * @param result - Result<Reservation> containing any possible error message to be displayed to the user
     * @param operation - specific CRUD operation that the user is trying to perform
     */
    private void showResultMessage(Result<Reservation> result, String operation) {
        if (result.isSuccess()) {
            view.displayHeader("Success");
            view.displayMessage("Reservation %sd.\n", operation);
        } else {
            view.displayErrors(result.getErrors());
        }
    }

    /**
     * Get a <T extends Client> based on a matching email; Offer the user the ability
     * to view a list of options based on State
     *
     * @param clientService - clientService that is used to get either a Host or
     *                        Guest based on their email.
     * @return a <T extends Client> on a matching email; non matching returns null
     */
    private <T extends Client> T getClient(ClientService<T> clientService, String client) {
        if (view.getFilterOnPrompt("Show " + client + " by State? ")) {
            State s = view.getState("Enter a US state: ");
            view.showClients(clientService.findByState(s));
        }
        String email = view.getEmail("Enter " + client + " Email: ");
        return clientService.findByEmail(email);
    }

    /**
     * Filters Reservation to remove those which occurred in the past.
     *
     * @param reservations - List<Reservation> to be filtered
     * @return a List<Reservation> that are currently taking place or have not occurred yet
     */
    private List<Reservation> getCurrentReservations(List<Reservation> reservations) {
        return reservations.stream()
                .filter(r -> r.getCheckOut().compareTo(LocalDate.now()) >= 0)
                .toList();
    };
}
