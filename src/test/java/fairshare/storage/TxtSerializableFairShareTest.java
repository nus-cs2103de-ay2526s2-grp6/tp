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

public class TxtSerializableFairShareTest {

    @TempDir
    Path tempDir;

    private Path testFilePath;
    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    public void setUp() {
        testFilePath = tempDir.resolve("test_expenses.txt");

        Group group = new Group("malaysia");
        Person alice = new Person("alice");
        Person bob = new Person("bob");

        expense1 = new Expense(
                group, "lunch", 20.0, alice,
                new HashSet<>(List.of(
                        new Participant(alice, 1),
                        new Participant(bob, 1))),
                new HashSet<>(List.of(new Tag("food"))),
                ExpenseType.EXPENSE);

        expense2 = new Expense(
                group, "taxi", 30.0, bob,
                new HashSet<>(List.of(
                        new Participant(bob, 2),
                        new Participant(alice, 1))),
                new HashSet<>(List.of(new Tag("transport"))),
                ExpenseType.EXPENSE);
    }

    @Test
    public void constructor_validExpenses_correctSize() {
        TxtSerializableFairShare serializable =
                new TxtSerializableFairShare(
                        List.of(expense1, expense2));
        assertEquals(2, serializable.getExpenses().size());
    }

    @Test
    public void toModelType_validExpenses_correctData() {
        TxtSerializableFairShare serializable =
                new TxtSerializableFairShare(
                        List.of(expense1, expense2));
        List<Expense> result = serializable.toModelType();

        assertEquals(2, result.size());
        assertEquals("lunch", result.get(0).getExpenseName());
        assertEquals("taxi", result.get(1).getExpenseName());
    }

    @Test
    public void saveToFile_validExpenses_createsFile()
            throws IOException {
        TxtSerializableFairShare serializable =
                new TxtSerializableFairShare(
                        List.of(expense1, expense2));
        serializable.saveToFile(testFilePath);

        assertTrue(Files.exists(testFilePath));
    }

    @Test
    public void saveToFile_validExpenses_correctLineCount()
            throws IOException {
        TxtSerializableFairShare serializable =
                new TxtSerializableFairShare(
                        List.of(expense1, expense2));
        serializable.saveToFile(testFilePath);

        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(2, lines.size());
    }

    @Test
    public void saveToFile_createsParentDirectories()
            throws IOException {
        Path nestedPath = tempDir.resolve("nested/dir/expenses.txt");
        TxtSerializableFairShare serializable =
                new TxtSerializableFairShare(List.of(expense1));
        serializable.saveToFile(nestedPath);

        assertTrue(Files.exists(nestedPath));
    }

    @Test
    public void loadFromFile_validFile_correctExpenseCount()
            throws IOException {
        TxtSerializableFairShare serializable =
                new TxtSerializableFairShare(
                        List.of(expense1, expense2));
        serializable.saveToFile(testFilePath);

        TxtSerializableFairShare loaded =
                TxtSerializableFairShare.loadFromFile(testFilePath);
        assertEquals(2, loaded.toModelType().size());
    }

    @Test
    public void loadFromFile_validFile_correctData()
            throws IOException {
        TxtSerializableFairShare serializable =
                new TxtSerializableFairShare(
                        List.of(expense1, expense2));
        serializable.saveToFile(testFilePath);

        List<Expense> loaded = TxtSerializableFairShare
                .loadFromFile(testFilePath)
                .toModelType();

        assertEquals("MALAYSIA",
                loaded.get(0).getGroup().getGroupName());
        assertEquals("lunch", loaded.get(0).getExpenseName());
        assertEquals(20.0, loaded.get(0).getAmount());
        assertEquals("alice",
                loaded.get(0).getPayer().getName());
    }

    @Test
    public void loadFromFile_validFile_correctShares() throws IOException {
        TxtSerializableFairShare serializable = new TxtSerializableFairShare(List.of(expense2));
        serializable.saveToFile(testFilePath);

        List<Expense> loaded = TxtSerializableFairShare
                .loadFromFile(testFilePath)
                .toModelType();

        Set<Participant> loadedParticipants = loaded.get(0).getParticipants();

        Participant bob = loadedParticipants.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("bob"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("bob should be in the loaded set"));

        Participant alice = loadedParticipants.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("alice"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("alice should be in the loaded set"));

        assertEquals(2, bob.getShares());
        assertEquals(1, alice.getShares());
    }

    @Test
    public void loadFromFile_invalidFormat_throwsException()
            throws IOException {
        Files.writeString(testFilePath, "corrupted|data");
        assertThrows(Exception.class, () ->
                TxtSerializableFairShare.loadFromFile(testFilePath));
    }

    @Test
    public void saveAndLoad_roundTrip_sameData()
            throws IOException {
        TxtSerializableFairShare original =
                new TxtSerializableFairShare(
                        List.of(expense1, expense2));
        original.saveToFile(testFilePath);

        List<Expense> loaded = TxtSerializableFairShare
                .loadFromFile(testFilePath)
                .toModelType();

        assertEquals(expense1.getExpenseName(),
                loaded.get(0).getExpenseName());
        assertEquals(expense1.getAmount(),
                loaded.get(0).getAmount());
        assertEquals(expense2.getExpenseName(),
                loaded.get(1).getExpenseName());
        assertEquals(expense2.getAmount(),
                loaded.get(1).getAmount());
    }

    @Test
    public void saveAndLoad_emptyList_returnsEmptyList()
            throws IOException {
        TxtSerializableFairShare serializable =
                new TxtSerializableFairShare(new ArrayList<>());
        serializable.saveToFile(testFilePath);

        List<Expense> loaded = TxtSerializableFairShare
                .loadFromFile(testFilePath)
                .toModelType();

        assertTrue(loaded.isEmpty());
    }

    @Test
    public void saveAndLoad_settlement_correctExpenseType()
            throws IOException {
        Group group = new Group("malaysia");
        Person alice = new Person("alice");
        Person bob = new Person("bob");

        Expense settlement = new Expense(
                group, "Settlement", 10.0, alice,
                new HashSet<>(List.of(new Participant(bob, 1))),
                new HashSet<>(),
                ExpenseType.SETTLEMENT);

        TxtSerializableFairShare serializable =
                new TxtSerializableFairShare(List.of(settlement));
        serializable.saveToFile(testFilePath);

        List<Expense> loaded = TxtSerializableFairShare
                .loadFromFile(testFilePath)
                .toModelType();

        assertEquals(ExpenseType.SETTLEMENT,
                loaded.get(0).getExpenseType());
    }
}
