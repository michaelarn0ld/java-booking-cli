package michaelarn0ld.mastery.data;

import michaelarn0ld.mastery.data.abstractions.ClientFileRepository;
import michaelarn0ld.mastery.data.contracts.ClientRepository;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.State;

import java.math.BigDecimal;

public class HostFileRepository extends ClientFileRepository<Host> implements ClientRepository<Host> {

    /*
    CONSTRUCTOR
     */
    public HostFileRepository(String filePath) {
        super(filePath);
        this.FIELDS = 10;
    }

    /**
     * Takes a String[] and produces a Host if it has an exactly equivalent number of
     * fields; each respective field of fields is expected to be convertible to the
     * appropriate Host field type.
     *
     * @param fields - parsing a line of csv data produces a String[] of Host data
     * @return a Host instance built from a line of the csv data
     */
    protected Host deserialize(String[] fields) {
        Host host = new Host();
        host.setId(fields[0]);
        host.setLastName(fields[1]);
        host.setEmail(fields[2]);
        host.setPhoneNumber(fields[3]);
        host.setStreetAddress(fields[4]);
        host.setCity(fields[5]);
        host.setState(State.valueOf(fields[6]));
        host.setPostalCode(fields[7]);
        host.setStandardRate(new BigDecimal(fields[8]));
        host.setWeekendRate(new BigDecimal(fields[9]));
        return host;
    }
}
