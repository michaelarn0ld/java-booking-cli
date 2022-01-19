package michaelarn0ld.mastery.data;

import michaelarn0ld.mastery.data.abstractions.ClientFileRepository;
import michaelarn0ld.mastery.data.contracts.ClientRepository;
import michaelarn0ld.mastery.models.Guest;
import michaelarn0ld.mastery.models.State;

public class GuestFileRepository extends ClientFileRepository<Guest> implements ClientRepository<Guest> {

    /*
    CONSTRUCTOR
     */
    public GuestFileRepository(String filePath) {
        super(filePath);
        this.FIELDS = 6;
    }

    /**
     * Takes a String[] and produces a Guest if it has an exactly equivalent number of
     * fields; each respective field of fields is expected to be convertible to the
     * appropriate Guest field type.
     *
     * @param fields - parsing a line of csv data produces a String[] of Guest data
     * @return a Guest instance built from a line of the csv data
     */
    protected Guest deserialize(String[] fields) {
        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[0]));
        guest.setFirstName(fields[1]);
        guest.setLastName(fields[2]);
        guest.setEmail(fields[3]);
        guest.setPhoneNumber(fields[4]);
        guest.setState(State.valueOf(fields[5]));
        return guest;
    }
}
