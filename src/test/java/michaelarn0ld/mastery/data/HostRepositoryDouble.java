package michaelarn0ld.mastery.data;

import michaelarn0ld.mastery.data.contracts.ClientRepository;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.State;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements ClientRepository<Host> {
    private final ArrayList<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() {
        Host h1 = new Host();
        h1.setId("test-id-1");
        h1.setLastName("Bastrop");
        h1.setEmail("ebastrop@wework.io");
        h1.setPhoneNumber("(444) 4444444");
        h1.setStreetAddress("123 Wonder St.");
        h1.setCity("Corvallis");
        h1.setState(State.OR);
        h1.setPostalCode("97333");
        h1.setStandardRate(BigDecimal.valueOf(120.99));
        h1.setWeekendRate(BigDecimal.valueOf(189.95));

        Host h2 = new Host();
        h2.setId("test-id-2");
        h2.setLastName("Allon");
        h2.setEmail("barballon@gmail.com");
        h2.setPhoneNumber("(444) 4425574");
        h2.setStreetAddress("456 Evergreen Ave.");
        h2.setCity("Eugene");
        h2.setState(State.OR);
        h2.setPostalCode("97408");
        h2.setStandardRate(BigDecimal.valueOf(100.90));
        h2.setWeekendRate(BigDecimal.valueOf(200.05));

        hosts.add(h1);
        hosts.add(h2);
    }

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public List<Host> findByState(State s) {
        return hosts.stream()
                .filter(h -> h.getState() == s)
                .toList();
    }

    @Override
    public Host findByEmail(String email) {
        return hosts.stream()
                .filter(h -> h.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
