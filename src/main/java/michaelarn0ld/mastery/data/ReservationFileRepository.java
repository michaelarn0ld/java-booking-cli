package michaelarn0ld.mastery.data;

import michaelarn0ld.mastery.data.abstractions.FileRepository;
import michaelarn0ld.mastery.data.contracts.ReservationRepository;
import michaelarn0ld.mastery.data.exceptions.DataException;
import michaelarn0ld.mastery.models.Guest;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.Reservation;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository {

    /*
    FIELDS
     */
    private final String directory;
    private final static String HEADER = "id,start_date,end_date,guest_id,total";

    /*
    CONSTRUCTOR
     */
    public ReservationFileRepository(String directory) {
        this.directory = directory;
    }

    /**
     * Finds all Reservation associated with a given Host
     *
     * @param h - Host whose Reservation data will be deserialized
     * @return all Reservation associated with the Host
     */
    @Override
    public List<Reservation> findByHost(Host h) {
        return new HostReservationFileRepository(h).findAll();
    }

    /**
     * Find a Reservation with a matching id, specific to its Host
     *
     * @param h - Host whose Reservation will be searched
     * @param id - Host specific unique identifier to match on
     * @return a Reservation with matching Host and id
     */
    @Override
    public Reservation findById(Host h, int id) {
        return new HostReservationFileRepository(h).findAll().stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * See line 100
     */
    @Override
    public Reservation add(Reservation r) throws DataException {
        return new HostReservationFileRepository(r.getHost()).add(r);
    }

    /**
     * See line 114
     */
    @Override
    public boolean update(Reservation r) throws DataException {
        return new HostReservationFileRepository(r.getHost()).update(r);
    }

    /**
     * See line 133
     */
    @Override
    public boolean delete(Reservation r) throws DataException {
        return new HostReservationFileRepository(r.getHost()).delete(r);
    }

    /**
     * Private implementation repository; accepts a Host used to identify the file used for CRUD operations.
     */
    private class HostReservationFileRepository extends FileRepository<Reservation> {

        /*
        FIELDS
         */
        private final Host h;

        /*
        CONSTRUCTOR
         */
        public HostReservationFileRepository(Host h) {
            this.h = h;
            this.filePath = getFilePath(h);
            this.FIELDS = 5;
        }

        /**
         * Adds a Reservation to the associated Host file
         *
         * @param r - Reservation to be written to the file
         * @return the Reservation with the added id to the caller
         */
        public Reservation add(Reservation r) throws DataException {
            List<Reservation> all = findByHost(r.getHost());
            r.setId(getNextId(all));
            all.add(r);
            writeAll(all);
            return r;
        }

        /**
         * Updates an existing Reservation associated with a Host
         *
         * @param r - Reservation to be updated
         * @return true if the update is successful
         */
        public boolean update(Reservation r) throws DataException {
            List<Reservation> all = findByHost(r.getHost());
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == r.getId()) {
                    all.set(i, r);
                    writeAll(all);
                    return true;
                }
            }
            return false;
        }

        /**
         * Deletes an existing Reservation associated with a Host
         *
         * @param r - Reservation to be deleted
         * @return true if delete is successful
         */
        public boolean delete(Reservation r) throws DataException {
            List<Reservation> all = findByHost(r.getHost());
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == r.getId()) {
                    all.remove(i);
                    writeAll(all);
                    return true;
                }
            }
            return false;
        }

        /**
         * Takes a String[] and produces a Reservation if it has an exactly equivalent
         * number of fields; each respective field of fields is expected to be convertible
         * to the appropriate Guest field type.
         *
         * @param fields - parsing csv line produces a String[] of Reservation data
         * @return a Reservation instance built from a line of the csv data
         */
        @Override
        protected Reservation deserialize(String[] fields) {
            Reservation r = new Reservation();
            r.setId(Integer.parseInt(fields[0]));
            r.setCheckIn(LocalDate.parse(fields[1]));
            r.setCheckOut(LocalDate.parse(fields[2]));
            r.setHost(h);
            Guest g = new Guest();
            g.setId(Integer.parseInt(fields[3]));
            r.setGuest(g);
            return r;
        }

        /**
         * Turns a Reservation into a String suitable for the csv file.
         *
         * @param r - Reservation to be parsed
         * @return String to be written to the data file
         */
        private String serialize(Reservation r) {
            return String.format("%s,%s,%s,%s,%s",
                    r.getId(),
                    r.getCheckIn(),
                    r.getCheckOut(),
                    r.getGuest().getId(),
                    r.getTotal());
        }

        /**
         * Overwrites the existing host file with fresh serialized Reservation instances
         *
         * @param reservations - Reservation instances to be serialized and parsed
         */
        private void writeAll(List<Reservation> reservations) throws DataException {
            try (PrintWriter writer = new PrintWriter(filePath)) {
                writer.println(HEADER);
                reservations.forEach(r -> writer.println(serialize(r)));
            } catch (IOException ex) {
                throw new DataException(ex.getMessage(), ex);
            }
        }

        /**
         * Returns a unique id to be attached to a Reservation that is specific to a
         * Host; Differing Host files may have the same Reservation id.
         *
         * @param reservations - List<Reservation> which is used to get the largest
         *                     current id
         * @return - the previous maximum id incremented by 1
         */
        private int getNextId(List<Reservation> reservations) {
            return reservations.stream()
                    .map(Reservation::getId)
                    .reduce(Integer::max)
                    .orElse(0) + 1;
        }

        /**
         * Gets the full file path where all Reservation associated with a specific
         * Host live
         *
         * @param h - Host whose id is used as a unique file identifier
         * @return String that is the path (from root of project) to the Host file
         */
        private String getFilePath(Host h) {
            return Paths.get(directory, h.getId() + ".csv").toString();
        }

    }
}