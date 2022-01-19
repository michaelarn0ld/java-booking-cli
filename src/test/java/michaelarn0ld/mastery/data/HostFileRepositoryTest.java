package michaelarn0ld.mastery.data;

import michaelarn0ld.mastery.data.exceptions.DataException;
import michaelarn0ld.mastery.models.Host;
import michaelarn0ld.mastery.models.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    private static final String SEED_PATH = "./data/hosts_seed.csv";
    private static final String TEST_PATH = "./data/hosts_test.csv";

    HostFileRepository repo = new HostFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Host> all = repo.findAll();
        assertEquals(4, all.size());
    }

    @Test
    void shouldFindByState() {
        List<Host> texans = repo.findByState(State.TX);
        assertEquals(2, texans.size());

        List<Host> georgians = repo.findByState(State.GA);
        assertEquals(1, georgians.size());
    }

    @Test
    void shouldNotFindWrongState() {
        List<Host> noneFromDC = repo.findByState(State.DC);
        assertEquals(0, noneFromDC.size());
    }
}