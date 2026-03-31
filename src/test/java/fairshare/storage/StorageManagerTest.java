package fairshare.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fairshare.model.expense.ExpenseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import fairshare.model.expense.Expense;
import fairshare.model.expense.Participant;
import fairshare.model.group.Group;
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
    public void readFairShare_noFileExists_returnsEmptyList()
            throws StorageException {
        List<Expense> result = storageManager.readFairShare();
        assertTrue(result.isEmpty());
    }

    @Test
    public void saveAndRead_validExpenses_sameData()
            throws StorageException {
        Group group = new Group("malaysia");
        Person payer = new Person("alice");
        ExpenseType expenseType = ExpenseType.EXPENSE;
        List<Participant> participants = new ArrayList<>(
                List.of(
                        new Participant(payer, 1),
                        new Participant(new Person("bob"), 2)));
        List<Tag> tags = new ArrayList<>(List.of(new Tag("food")));
        Expense expense = new Expense(group, "lunch", 20.0,
                payer, participants, tags, expenseType);

        List<Expense> expenses = new ArrayList<>(List.of(expense));
        storageManager.saveFairShare(expenses);

        List<Expense> loaded = storageManager.readFairShare();
        assertEquals(1, loaded.size());
        assertEquals("malaysia", loaded.get(0).getGroup().getGroupName());
        assertEquals("lunch", loaded.get(0).getExpenseName());
        assertEquals(20.0, loaded.get(0).getAmount());
        assertEquals("alice", loaded.get(0).getPayer().getName());
        assertEquals(2, loaded.get(0).getParticipants().size());
    }

    @Test
    public void saveAndRead_correctShares_preserved()
            throws StorageException {
        Group group = new Group("malaysia");
        Person payer = new Person("alice");
        ExpenseType expenseType = ExpenseType.EXPENSE;
        List<Participant> participants = new ArrayList<>(
                List.of(
                        new Participant(new Person("bob"), 2),
                        new Participant(new Person("mary"), 1)));
        List<Tag> tags = new ArrayList<>(List.of(new Tag("food")));
        Expense expense = new Expense(group, "lunch", 30.0,
                payer, participants, tags, expenseType);

        storageManager.saveFairShare(List.of(expense));
        List<Expense> loaded = storageManager.readFairShare();

        assertEquals(2,
                loaded.get(0).getParticipants().get(0).getShares());
        assertEquals(1,
                loaded.get(0).getParticipants().get(1).getShares());
    }

    @Test
    public void saveAndRead_multipleExpenses_allPreserved()
            throws StorageException {
        Group group = new Group("malaysia");
        Person alice = new Person("alice");
        Person bob = new Person("bob");
        ExpenseType expenseType = ExpenseType.EXPENSE;

        List<Participant> participants1 = new ArrayList<>(
                List.of(new Participant(alice, 1),
                        new Participant(bob, 1)));
        List<Participant> participants2 = new ArrayList<>(
                List.of(new Participant(bob, 2),
                        new Participant(alice, 1)));

        Expense expense1 = new Expense(group, "lunch", 20.0, alice,
                participants1, List.of(new Tag("food")), expenseType);
        Expense expense2 = new Expense(group, "taxi", 30.0, bob,
                participants2, List.of(new Tag("transport")), expenseType);

        storageManager.saveFairShare(List.of(expense1, expense2));
        List<Expense> loaded = storageManager.readFairShare();

        assertEquals(2, loaded.size());
        assertEquals("lunch", loaded.get(0).getExpenseName());
        assertEquals("taxi", loaded.get(1).getExpenseName());
    }

    @Test
    public void readFairShare_corruptedFile_throwsStorageException()
            throws IOException {
        Files.writeString(testFilePath, "this is corrupted data");

        assertThrows(StorageException.class, () ->
                storageManager.readFairShare());
    }

    @Test
    public void readFairShare_corruptedFile_deletesFile()
            throws IOException {
        Files.writeString(testFilePath, "this is corrupted data");

        try {
            storageManager.readFairShare();
        } catch (StorageException e) {
            // expected
        }

        assertTrue(!Files.exists(testFilePath));
    }

    @Test
    public void readFairShare_oldFormatWithoutShares_throwsException()
            throws IOException {
        Files.writeString(testFilePath,
                "lunch|30.0|alice|bob,mary|food");

        assertThrows(StorageException.class, () ->
                storageManager.readFairShare());
    }

    @Test
    public void getFairShareFilePath_returnsCorrectPath() {
        assertEquals(testFilePath,
                storageManager.getFairShareFilePath());
    }
}
