package michaelarn0ld.mastery.data;

import michaelarn0ld.mastery.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    private static final String SEED_PATH = "./data/guests_seed.csv";
    private static final String TEST_PATH = "./data/guests_test.csv";

    GuestFileRepository repo = new GuestFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Guest> all = repo.findAll();
        assertEquals(4, all.size());
    }

    @Test
    void shouldFindById() {
        Guest sullivanLomas = repo.findByEmail("slomas0@mediafire.com");
        assertEquals("Sullivan", sullivanLomas.getFirstName());
    }

    @Test
    void shouldNotFindWrongId() {
        Guest nobody = repo.findByEmail("Nobody@nobody.com");
        assertNull(nobody);
    }
}