package fairshare.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fairshare.model.expense.Expense;
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
        List<Participant> participants = new ArrayList<>(
                List.of(
                        new Participant(payer, 1),
                        new Participant(new Person("bob"), 2),
                        new Participant(new Person("carol"), 1)));
        List<Tag> tags = new ArrayList<>(
                List.of(new Tag("food"), new Tag("trip")));

        expense = new Expense(group, "lunch", 30.0, payer, participants, tags);
        adaptedExpense = new TxtAdaptedExpense(expense);
    }

    @Test
    public void serialise_validExpense_correctFormat() {
        String serialised = adaptedExpense.serialize();
        assertEquals("lunch|30.0|alice|alice:1,bob:2,carol:1|food,trip",
                serialised);
    }

    @Test
    public void deserialise_validLine_correctFields() {
        String line = "lunch|30.0|alice|alice:1,bob:2,carol:1|food,trip";
        TxtAdaptedExpense result = TxtAdaptedExpense.deserialize(line);

        assertEquals("lunch", result.getExpenseName());
        assertEquals(30.0, result.getAmount());
        assertEquals("alice", result.getPayer().getName());
        assertEquals(3, result.getParticipants().size());
        assertEquals(2, result.getTags().size());
    }

    @Test
    public void deserialise_validLine_correctShares() {
        String line = "lunch|30.0|alice|alice:1,bob:2,carol:1|food,trip";
        TxtAdaptedExpense result = TxtAdaptedExpense.deserialize(line);

        assertEquals("bob", result.getParticipants().get(1).getName());
        assertEquals(2, result.getParticipants().get(1).getShares());
    }

    @Test
    public void deserialize_invalidFormat_throwsException() {
        String invalidLine = "lunch|30.0|alice";
        assertThrows(IllegalArgumentException.class, () ->
                TxtAdaptedExpense.deserialize(invalidLine));
    }

    @Test
    public void deserialise_invalidParticipantFormat_throwsException() {
        String invalidLine = "lunch|30.0|alice|bob|food";
        assertThrows(IllegalArgumentException.class, () ->
                TxtAdaptedExpense.deserialize(invalidLine));
    }

    @Test
    public void toModelType_validAdaptedExpense_correctExpense() {
        Expense result = adaptedExpense.toModelType();

        assertEquals("lunch", result.getExpenseName());
        assertEquals(30.0, result.getAmount());
        assertEquals("alice", result.getPayer().getName());
        assertEquals(3, result.getParticipants().size());
        assertEquals(2, result.getTags().size());
    }

    @Test
    public void toModelType_correctShares() {
        Expense result = adaptedExpense.toModelType();

        assertEquals("bob",
                result.getParticipants().get(1).getPerson().getName());
        assertEquals(2,
                result.getParticipants().get(1).getShares());
    }

    @Test
    public void serialiseAndDeserialize_roundTrip_sameData() {
        String serialised = adaptedExpense.serialize();
        TxtAdaptedExpense deserialized =
                TxtAdaptedExpense.deserialize(serialised);
        Expense result = deserialized.toModelType();

        assertEquals(expense.getExpenseName(), result.getExpenseName());
        assertEquals(expense.getAmount(), result.getAmount());
        assertEquals(expense.getPayer().getName(),
                result.getPayer().getName());
        assertEquals(expense.getParticipants().size(),
                result.getParticipants().size());
        assertEquals(expense.getTags().size(), result.getTags().size());
    }

    @Test
    public void serialiseAndDeserialize_roundTrip_correctShares() {
        String serialized = adaptedExpense.serialize();
        TxtAdaptedExpense deserialized =
                TxtAdaptedExpense.deserialize(serialized);
        Expense result = deserialized.toModelType();

        assertEquals(
                expense.getParticipants().get(1).getShares(),
                result.getParticipants().get(1).getShares());
    }
}
