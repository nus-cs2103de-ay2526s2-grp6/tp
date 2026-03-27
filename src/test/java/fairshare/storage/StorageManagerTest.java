package fairshare.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import fairshare.model.expense.Expense;
import fairshare.model.expense.Participant;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;
import fairshare.storage.exceptions.StorageException;

public class StorageManagerTest {

    @TempDir
    Path tempDir;

    private StorageManager storageManager;
    private Path testFilePath;

    @BeforeEach
    public void setUp() {
        testFilePath = tempDir.resolve("test_expenses.txt");
        storageManager = new StorageManager(
                new TxtFairShareStorage(testFilePath));
    }

    @Test
    public void readExpenseTracker_noFileExists_returnsEmptyList()
            throws StorageException {
        List<Expense> result = storageManager.readFairShare();
        assertTrue(result.isEmpty());
    }

    @Test
    public void saveAndRead_validExpenses_sameData() throws StorageException {
        Person payer = new Person("alice");
        List<Participant> participants = new ArrayList<>(
                List.of(new Participant(payer, 1), new Participant(new Person("bob"), 1)));
        List<Tag> tags = new ArrayList<>(List.of(new Tag("food")));
        Expense expense = new Expense("lunch", 20.0,
                payer, participants, tags);

        List<Expense> expenses = new ArrayList<>(List.of(expense));
        storageManager.saveFairShare(expenses);

        List<Expense> loaded = storageManager.readFairShare();
        assertEquals(1, loaded.size());
        assertEquals("lunch", loaded.get(0).getExpenseName());
        assertEquals(20.0, loaded.get(0).getAmount());
        assertEquals("alice", loaded.get(0).getPayer().getName());
    }

    @Test
    public void getExpenseTrackerFilePath_returnsCorrectPath() {
        assertEquals(testFilePath,
                storageManager.getFairShareFilePath());
    }
}
