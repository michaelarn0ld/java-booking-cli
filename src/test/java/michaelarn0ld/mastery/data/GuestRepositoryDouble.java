package michaelarn0ld.mastery.data;

import michaelarn0ld.mastery.data.contracts.ClientRepository;
import michaelarn0ld.mastery.models.Guest;
import michaelarn0ld.mastery.models.State;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements ClientRepository<Guest> {
    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {
        Guest g1 = new Guest();
        g1.setId(1);
        g1.setFirstName("Bob");
        g1.setLastName("Dale");
        g1.setEmail("bdale@gmail.com");
        g1.setPhoneNumber("(555) 5555555");
        g1.setState(State.TX);

        Guest g2 = new Guest();
        g2.setId(2);
        g2.setFirstName("Adi");
        g2.setLastName("Oz");
        g2.setEmail("ahoz23@gmail.com");
        g2.setPhoneNumber("(888) 8888888");
        g2.setState(State.OR);

        Guest g3 = new Guest();
        g3.setId(3);
        g3.setFirstName("Michael");
        g3.setLastName("Arnold");
        g3.setEmail("me@michaelarnold.io");
        g3.setPhoneNumber("(999) 9999999");
        g3.setState(State.TX);

        guests.add(g1);
        guests.add(g2);
        guests.add(g3);
    }

    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public List<Guest> findByState(State s) {
        return guests.stream()
                .filter(g -> g.getState() == s)
                .toList();
    }

    @Override
    public Guest findByEmail(String email) {
        return guests.stream()
                .filter(g -> g.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
