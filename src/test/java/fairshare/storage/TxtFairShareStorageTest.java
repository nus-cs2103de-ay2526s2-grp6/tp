package fairshare.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.model.expense.Participant;
import fairshare.model.group.Group;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;
import fairshare.storage.exceptions.StorageException;

public class TxtFairShareStorageTest {

    @TempDir
    Path tempDir;

    private TxtFairShareStorage storage;
    private Path testFilePath;
    private Expense expense;

    @BeforeEach
    public void setUp() {
        testFilePath = tempDir.resolve("expenses.txt");
        storage = new TxtFairShareStorage(testFilePath);

        Group group = new Group("malaysia");
        Person alice = new Person("alice");
        expense = new Expense(
                group, "lunch", 20.0, alice,
                new HashSet<>(List.of(
                        new Participant(alice, 1),
                        new Participant(new Person("bob"), 1))),
                new HashSet<>(List.of(new Tag("food"))),
                ExpenseType.EXPENSE);
    }

    @Test
    public void getFairShareFilePath_returnsCorrectPath() {
        assertEquals(testFilePath, storage.getFairShareFilePath());
    }

    @Test
    public void readFairShare_noFileExists_returnsEmptyList()
            throws StorageException {
        List<Expense> result = storage.readFairShare();
        assertTrue(result.isEmpty());
    }

    @Test
    public void saveFairShare_validExpenses_createsFile()
            throws StorageException {
        storage.saveFairShare(List.of(expense));
        assertTrue(Files.exists(testFilePath));
    }

    @Test
    public void saveFairShare_validExpenses_canBeReadBack()
            throws StorageException {
        storage.saveFairShare(List.of(expense));
        List<Expense> loaded = storage.readFairShare();

        assertEquals(1, loaded.size());
        assertEquals("lunch", loaded.get(0).getExpenseName());
        assertEquals(20.0, loaded.get(0).getAmount());
        assertEquals("alice", loaded.get(0).getPayer().getName());
        assertEquals("MALAYSIA",
                loaded.get(0).getGroup().getGroupName());
    }

    @Test
    public void saveFairShare_emptyList_savesEmptyFile()
            throws StorageException {
        storage.saveFairShare(new ArrayList<>());
        assertTrue(Files.exists(testFilePath));
    }

    @Test
    public void readFairShare_emptyFile_returnsEmptyList()
            throws StorageException {
        storage.saveFairShare(new ArrayList<>());
        List<Expense> result = storage.readFairShare();
        assertTrue(result.isEmpty());
    }

    @Test
    public void readFairShare_corruptedFile_throwsStorageException()
            throws IOException {
        Files.writeString(testFilePath, "this is corrupted");
        assertThrows(StorageException.class, () ->
                storage.readFairShare());
    }

    @Test
    public void readFairShare_corruptedFile_deletesFile()
            throws IOException {
        Files.writeString(testFilePath, "this is corrupted");
        try {
            storage.readFairShare();
        } catch (StorageException e) {
            // expected
        }
        assertTrue(!Files.exists(testFilePath));
    }

    @Test
    public void readFairShare_afterCorruption_returnsEmptyList()
            throws IOException, StorageException {
        Files.writeString(testFilePath, "this is corrupted");
        try {
            storage.readFairShare();
        } catch (StorageException e) {
            // expected — file is now deleted
        }
        List<Expense> result = storage.readFairShare();
        assertTrue(result.isEmpty());
    }

    @Test
    public void saveFairShare_multipleExpenses_allPreserved()
            throws StorageException {
        Group group = new Group("malaysia");
        Person bob = new Person("bob");
        Expense expense2 = new Expense(
                group, "taxi", 30.0, bob,
                new HashSet<>(List.of(
                        new Participant(bob, 2),
                        new Participant(new Person("alice"), 1))),
                new HashSet<>(List.of(new Tag("transport"))),
                ExpenseType.EXPENSE);

        storage.saveFairShare(List.of(expense, expense2));
        List<Expense> loaded = storage.readFairShare();

        assertEquals(2, loaded.size());
        assertEquals("lunch", loaded.get(0).getExpenseName());
        assertEquals("taxi", loaded.get(1).getExpenseName());
    }

    @Test
    public void saveFairShare_settlement_preservesExpenseType()
            throws StorageException {
        Group group = new Group("malaysia");
        Person alice = new Person("alice");
        Expense settlement = new Expense(
                group, "Settlement", 10.0, alice,
                new HashSet<>(List.of(
                        new Participant(new Person("bob"), 1))),
                new HashSet<>(),
                ExpenseType.SETTLEMENT);

        storage.saveFairShare(List.of(settlement));
        List<Expense> loaded = storage.readFairShare();

        assertEquals(ExpenseType.SETTLEMENT,
                loaded.get(0).getExpenseType());
    }

    @Test
    public void saveFairShare_preservesParticipantShares()
            throws StorageException {
        storage.saveFairShare(List.of(expense));
        List<Expense> loaded = storage.readFairShare();

        Set<Participant> loadedParticipants = loaded.get(0).getParticipants();
        assertEquals(2, loadedParticipants.size());

        Participant alice = loadedParticipants.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("alice"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("alice should be in the set"));

        Participant bob = loadedParticipants.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("bob"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("bob should be in the set"));

        assertEquals(1, alice.getShares());
        assertEquals(1, bob.getShares());
    }

    @Test
    public void saveFairShare_overwritesPreviousData()
            throws StorageException {
        storage.saveFairShare(List.of(expense));

        Group group = new Group("japan");
        Person carol = new Person("carol");
        Expense newExpense = new Expense(
                group, "sushi", 50.0, carol,
                new HashSet<>(List.of(
                        new Participant(carol, 1))),
                new HashSet<>(),
                ExpenseType.EXPENSE);

        storage.saveFairShare(List.of(newExpense));
        List<Expense> loaded = storage.readFairShare();

        assertEquals(1, loaded.size());
        assertEquals("sushi", loaded.get(0).getExpenseName());
    }
}
