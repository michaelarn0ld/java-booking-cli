package michaelarn0ld.mastery.domain;

import michaelarn0ld.mastery.data.contracts.ClientRepository;
import michaelarn0ld.mastery.data.contracts.ReservationRepository;
import michaelarn0ld.mastery.data.exceptions.DataException;
import michaelarn0ld.mastery.models.Guest;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationService {

    private final ReservationRepository repository;
    private final ClientRepository<Host> hostRepository;
    private final Map<Integer, Guest> guestMap;

    public final static String RESERVATION_NULL_ERROR = "CANNOT ADD NULL RESERVATION";
    public final static String CHECKIN_NULL_ERROR = "RESERVATION MUST HAVE CHECK-IN";
    public final static String CHECKIN_IN_PAST_ERROR = "RESERVATION CHECK-IN MUST NOT BE IN PAST";
    public final static String CHECKOUT_NULL_ERROR = "RESERVATION MUST HAVE CHECK-OUT";
    public final static String GUEST_NULL_ERROR = "RESERVATION MUST HAVE GUEST";
    public final static String HOST_NULL_ERROR = "RESERVATION MUST HAVE HOST";
    public final static String RESERVATION_NOT_FOUND_ERROR = "RESERVATION NOT FOUND";
    public final static String OVERLAPPING_RESERVATION_ERROR = "RESERVATIONS CANNOT OVERLAP";
    public final static String CHECKIN_AFTER_CHECKOUT_ERROR = "RESERVATIONS CHECK-IN MUST BE AT LEAST 1 DAY BEFORE CHECK-OUT";
    public final static String GUEST_NOT_FOUND_ERROR = "GUEST NOT FOUND.";
    public final static String HOST_NOT_FOUND_ERROR = "HOST NOT FOUND.";

    public ReservationService(ReservationRepository repository, ClientRepository<Guest> guestRepository, ClientRepository<Host> hostRepository) {
        this.repository = repository;
        this.hostRepository = hostRepository;
        guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(Guest::getId, g -> g));
    }

    /**
     * Finds all Reservation for a given Host
     *
     * @param h - Host to find the Reservation data
     * @return - List<Reservation> associated with a Host
     */
    public List<Reservation> findByHost(Host h) {
        List<Reservation> result = repository.findByHost(h);
        result.forEach(
                r -> r.setGuest(guestMap.get(r.getGuest().getId()))
        );
        return result;
    }

    /**
     * Finds the Reservation with matching id for a given Host
     *
     * @param h - Host to match on
     * @param id - identifier, unique to Host, to match on
     * @return a Reservation with a matching Host and id
     */
    public Reservation findById(Host h, int id) {
        Reservation result = repository.findById(h, id);
        if (result != null) {
            result.setGuest(guestMap.get(result.getGuest().getId()));
        }
        return result;
    }

    /**
     * Add a Reservation, if it meets the validation criteria
     *
     * @param r - Reservation to be added
     * @return a Result<Reservation> indicating whether the add was successful
     */
    public Result<Reservation> add(Reservation r) throws DataException {
        Result<Reservation> result = validate(r);
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(repository.add(r));
        return result;
    }

    /**
     * Updates a Reservation, if it exists and the provided updates pass validation
     *
     * @param r - Reservation to update
     * @return a Result<Reservation> indicating if the update was successful
     */
    public Result<Reservation> update(Reservation r) throws DataException {
        Result<Reservation> result = validate(r);
        if (!result.isSuccess()){
            return result;
        }
        if (repository.update(r)) {
            result.setPayload(r);
        } else {
            result.addError(RESERVATION_NOT_FOUND_ERROR);
        }

        return result;
    }

    /**
     * Deletes a Reservation, if it exists.
     *
     * @param r - Reservation to be deleted
     * @return a Result<Reservation> indicating if delete was successful
     */
    public Result<Reservation> delete(Reservation r) throws DataException {
        Result<Reservation> result = new Result<>();
        if (!repository.delete(r)) {
            result.addError(RESERVATION_NOT_FOUND_ERROR);
        }
        return result;
    }

    /**
     * Validates that a Reservation can be added
     *
     * @param r - Reservation to be validated
     * @return a Result<Reservation> containing any errors
     */
    private Result<Reservation> validate(Reservation r) {
        Result<Reservation> result = new Result<>();
        validateNulls(r).forEach(result::addError);
        if (!result.isSuccess()){
            return result;
        }
        validateFields(r).forEach(result::addError);
        if (!result.isSuccess()){
            return result;
        }
        validateChildren(r).forEach(result::addError);
        return result;
    }

    /**
     * Checks if the Reservation contains any null fields
     *
     * @param r - Reservation to be validated
     * @return errors that indicate if there are any nulls
     */
    private List<String> validateNulls(Reservation r) {
        ArrayList<String> errors = new ArrayList<>();
        if (r == null) {
           errors.add(RESERVATION_NULL_ERROR);
           return errors;
        }
        if (r.getCheckIn() == null) {
            errors.add(CHECKIN_NULL_ERROR);
        }
        if (r.getCheckOut() == null) {
            errors.add(CHECKOUT_NULL_ERROR);
        }
        if (r.getGuest() == null) {
            errors.add(GUEST_NULL_ERROR);
        }
        if (r.getHost() == null) {
            errors.add(HOST_NULL_ERROR);
        }
        return errors;
    }

    /**
     * Checks that the Reservation check-in is before the check-out and ensures that
     * there are no overlapping reservations.
     *
     * @param r - Reservation to be validated
     * @return any errors that indicate failure
     */
    private List<String> validateFields(Reservation r) {
        ArrayList<String> errors = new ArrayList<>();
        if (r.getCheckIn().compareTo(r.getCheckOut()) >= 0) {
            errors.add(CHECKIN_AFTER_CHECKOUT_ERROR);
        }
        if (r.getCheckIn().compareTo(LocalDate.now()) < 0) {
            errors.add(CHECKIN_IN_PAST_ERROR);
        }
        if (overlappingReservation(r)){
            errors.add(OVERLAPPING_RESERVATION_ERROR);
        }
        return errors;
    }

    /**
     * Checks that the selected Guest and Host associated with the reservation exist
     *
     * @param r - Reservation to be validated
     * @return any errors that indicate either the Host or Guest does not exist
     */
    private List<String> validateChildren(Reservation r){
        ArrayList<String> errors = new ArrayList<>();
        if (hostRepository.findByEmail(r.getHost().getEmail()) == null) {
            errors.add(HOST_NOT_FOUND_ERROR);
        }
        if (guestMap.get(r.getGuest().getId()) == null) {
            errors.add(GUEST_NOT_FOUND_ERROR);
        }
        return errors;
    }

    /**
     * Checks if the Reservation overlaps an existing Reservation
     *
     * @param r - Reservation to be validated
     * @return true if the Reservation overlaps an existing
     */
    private boolean overlappingReservation(Reservation r) {
        List<Reservation> reservations = findByHost(r.getHost());
        for (Reservation reservation : reservations) {
           if(!(reservation.getCheckIn().compareTo(r.getCheckOut()) >= 0
           || reservation.getCheckOut().compareTo(r.getCheckIn()) <= 0)
           && reservation.getId() != r.getId()) {
               return true;
           }
        }
        return false;
    }
}
