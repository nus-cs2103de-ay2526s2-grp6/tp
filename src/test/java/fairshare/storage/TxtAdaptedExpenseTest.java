package fairshare.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.model.expense.Participant;
import fairshare.model.group.Group;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

public class TxtAdaptedExpenseTest {

    private Expense expense;
    private TxtAdaptedExpense adaptedExpense;

    @BeforeEach
    public void setUp() {
        Group group = new Group("malaysia");
        Person payer = new Person("alice");
        ExpenseType expenseType = ExpenseType.EXPENSE;
        Set<Participant> participants = new HashSet<>(
                List.of(
                        new Participant(payer, 1),
                        new Participant(new Person("bob"), 2),
                        new Participant(new Person("carol"), 1)));
        Set<Tag> tags = new HashSet<>(
                List.of(new Tag("food"), new Tag("trip")));

        expense = new Expense(group, "lunch", 30.0, payer,
                participants, tags, expenseType);
        adaptedExpense = new TxtAdaptedExpense(expense);
    }

    @Test
    public void serialize_validExpense_correctFormat() {
        String serialized = adaptedExpense.serialize();
        assertEquals(
                "MALAYSIA|lunch|30.0|alice|alice:1,bob:2,carol:1|food,trip|EXPENSE",
                serialized);
    }

    @Test
    public void deserialize_validLine_correctFields() {
        String line =
                "malaysia|lunch|30.0|alice|alice:1,bob:2,carol:1|food,trip|EXPENSE";
        Expense result = TxtAdaptedExpense.deserialize(line)
                .toModelType();

        assertEquals("MALAYSIA", result.getGroup().getGroupName());
        assertEquals("lunch", result.getExpenseName());
        assertEquals(30.0, result.getAmount());
        assertEquals("alice", result.getPayer().getName());
        assertEquals(3, result.getParticipants().size());
        assertEquals(2, result.getTags().size());
        assertEquals(ExpenseType.EXPENSE, result.getExpenseType());
    }

    @Test
    public void deserialise_validLine_correctShares() {
        String line =
                "malaysia|lunch|30.0|alice|alice:1,bob:2,carol:1|food,trip|EXPENSE";
        Expense result = TxtAdaptedExpense.deserialize(line)
                .toModelType();

        Set<Participant> participants = result.getParticipants();
        assertEquals(3, participants.size());

        Participant bob = participants.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("bob"))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Bob should be in the set"));
        assertEquals(2, bob.getShares());
    }

    @Test
    public void deserialise_validSettlement_correctFields() {
        String line =
                "malaysia|Settlement|10.0|alice|bob:1||SETTLEMENT";
        Expense result = TxtAdaptedExpense.deserialize(line)
                .toModelType();

        assertEquals("MALAYSIA", result.getGroup().getGroupName());
        assertEquals("Settlement", result.getExpenseName());
        assertEquals(10.0, result.getAmount());
        assertEquals("alice", result.getPayer().getName());
        assertEquals(1, result.getParticipants().size());
        assertEquals(ExpenseType.SETTLEMENT, result.getExpenseType());
    }

    @Test
    public void deserialize_invalidFormat_throwsException() {
        String invalidLine = "lunch|30.0|alice";
        assertThrows(IllegalArgumentException.class, () ->
                TxtAdaptedExpense.deserialize(invalidLine));
    }

    @Test
    public void deserialise_invalidParticipantFormat_throwsException() {
        String invalidLine =
                "malaysia|lunch|30.0|alice|bob|food|EXPENSE";
        assertThrows(IllegalArgumentException.class, () ->
                TxtAdaptedExpense.deserialize(invalidLine));
    }

    @Test
    public void toModelType_validAdaptedExpense_correctExpense() {
        Expense result = adaptedExpense.toModelType();

        assertEquals("MALAYSIA",
                result.getGroup().getGroupName());
        assertEquals("lunch", result.getExpenseName());
        assertEquals(30.0, result.getAmount());
        assertEquals("alice", result.getPayer().getName());
        assertEquals(3, result.getParticipants().size());
        assertEquals(2, result.getTags().size());
        assertEquals(ExpenseType.EXPENSE, result.getExpenseType());
    }

    @Test
    public void toModelType_correctShares() {
        Expense result = adaptedExpense.toModelType();

        Set<Participant> participants = result.getParticipants();

        Participant bob = participants.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("bob"))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "bob should be in the set"));
        assertEquals(2, bob.getShares());
    }

    @Test
    public void serialiseAndDeserialize_roundTrip_sameData() {
        String serialised = adaptedExpense.serialize();
        Expense result = TxtAdaptedExpense.deserialize(serialised)
                .toModelType();

        assertEquals(expense.getGroup().getGroupName(),
                result.getGroup().getGroupName());
        assertEquals(expense.getExpenseName(),
                result.getExpenseName());
        assertEquals(expense.getAmount(), result.getAmount());
        assertEquals(expense.getPayer().getName(),
                result.getPayer().getName());
        assertEquals(expense.getParticipants().size(),
                result.getParticipants().size());
        assertEquals(expense.getTags().size(),
                result.getTags().size());
        assertEquals(expense.getExpenseType(),
                result.getExpenseType());
    }

    @Test
    public void serialiseAndDeserialize_roundTrip_correctShares() {
        String serialized = adaptedExpense.serialize();
        Expense result = TxtAdaptedExpense.deserialize(serialized)
                .toModelType();

        Participant originalBob = expense.getParticipants().stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("bob"))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "bob should be in the serialized set"));

        Participant resultBob = result.getParticipants().stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("bob"))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "bob should be in the deserialized set"));

        assertEquals(originalBob.getShares(), resultBob.getShares());
    }
}
