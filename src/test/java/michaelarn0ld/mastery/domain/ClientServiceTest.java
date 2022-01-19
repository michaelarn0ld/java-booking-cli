package michaelarn0ld.mastery.domain;

import michaelarn0ld.mastery.data.GuestFileRepository;
import michaelarn0ld.mastery.data.GuestRepositoryDouble;
import michaelarn0ld.mastery.data.HostFileRepository;
import michaelarn0ld.mastery.data.HostRepositoryDouble;
import michaelarn0ld.mastery.data.contracts.ClientRepository;
import michaelarn0ld.mastery.data.exceptions.DataException;
import michaelarn0ld.mastery.models.Client;
import michaelarn0ld.mastery.models.Guest;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.State;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    ClientRepository<Guest> guestRepo = new GuestRepositoryDouble();
    ClientRepository<Host> hostRepo = new HostRepositoryDouble();
    ClientService<Guest> guestService = new ClientService<Guest>(guestRepo);
    ClientService<Host> hostService = new ClientService<Host>(hostRepo);

    @Test
    void guestRepoShouldFindAll() throws DataException {
        List<Guest> all = guestService.findAll();
        assertEquals(3, all.size());
    }

    @Test
    void guestRepoShouldFindByState() throws DataException {
        List<Guest> texans = guestService.findByState(State.TX);
        assertEquals(2, texans.size());
    }

    @Test
    void guestRepoShouldNotFindInvalidState() throws DataException {
        List<Guest> none = guestService.findByState(State.KS);
        assertEquals(0, none.size());
    }

    @Test
    void guestRepoShouldFindByEmail() throws DataException {
        Guest michael = guestService.findByEmail("me@michaelarnold.io");
        assertEquals("Michael", michael.getFirstName());
    }

    @Test
    void guestRepoShouldNotFindInvalidEmail() throws DataException {
        Guest invalidEmail = guestService.findByEmail("invalid@example.com");
        assertNull(invalidEmail);
    }
    @Test
    void hostRepoShouldFindAll() throws DataException {
        List<Host> all = hostService.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void hostRepoShouldFindByState() throws DataException {
        List<Host> twoFromOR = hostService.findByState(State.OR);
        assertEquals(2, twoFromOR.size());
    }

    @Test
    void hostRepoShouldNotFindInvalidState() throws DataException {
        List<Host> noneFromDC = hostService.findByState(State.DC);
        assertEquals(0, noneFromDC.size());
    }

    @Test
    void hostRepoShouldFindByEmail() throws DataException {
        Host allon = hostService.findByEmail("barballon@gmail.com");
        assertEquals("test-id-2", allon.getId());
    }

    @Test
    void hostRepoShouldNotFindInvalidEmail() throws DataException {
        Host notHere = hostService.findByEmail("notaperson@gmail.com");
        assertNull(notHere);
    }
}