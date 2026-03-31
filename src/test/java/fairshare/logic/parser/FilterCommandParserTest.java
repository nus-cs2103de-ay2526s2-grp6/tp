package fairshare.logic.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fairshare.logic.commands.FilterCommand;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Expense;
import fairshare.model.expense.Participant;
import fairshare.model.group.Group;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

public class FilterCommandParserTest {
    private FilterCommandParser parser;
    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    public void setUp() {
        parser = new FilterCommandParser();

        Group expense1Group = new Group("malaysia");
        String expense1Name = "lunch";
        double expense1Amt = 25.0d;
        Person expense1Payer = new Person("john");
        List<Participant> expense1Participants = new ArrayList<>(
                List.of(
                        new Participant(expense1Payer, 1),
                        new Participant(new Person("alice"), 1),
                        new Participant(new Person("jamie"), 1)));
        List<Tag> expense1Tags = new ArrayList<>(List.of(new Tag("food"), new Tag("jb")));
        expense1 = new Expense(expense1Group, expense1Name, expense1Amt,
                expense1Payer, expense1Participants, expense1Tags);

        Group expense2Group = new Group("japan");
        String expense2Name = "dinner";
        double expense2Amt = 54.0d;
        Person expense2Payer = new Person("alice");
        List<Participant> expense2Participants = new ArrayList<>(
                List.of(
                        new Participant(expense2Payer, 1),
                        new Participant(new Person("jamie"), 1),
                        new Participant(new Person("carrie"), 1)));
        List<Tag> expense2Tags = new ArrayList<>(List.of(new Tag("food"), new Tag("school")));
        expense2 = new Expense(expense2Group, expense2Name, expense2Amt,
                expense2Payer, expense2Participants, expense2Tags);
    }

    // Test filtering by name
    @Test
    public void parseNameFieldPresent() throws ParseException {
        // Single field
        FilterCommand cmd = parser.parse("n/lunch");
        Predicate<Expense> predicate1 = cmd.getPredicate();
        assertTrue(predicate1.test(expense1));
        assertFalse(predicate1.test(expense2));

        // Multiple field
        FilterCommand cmd2 = parser.parse("n/lunch n/dinner");
        Predicate<Expense> predicate2 = cmd2.getPredicate();
        assertTrue(predicate2.test(expense1));
        assertTrue(predicate2.test(expense2));
    }

    // Test filtering by tag
    @Test
    public void parseTagFieldPresent() throws ParseException {
        // Single field
        FilterCommand cmd = parser.parse("t/jb");
        Predicate<Expense> predicate1 = cmd.getPredicate();
        assertTrue(predicate1.test(expense1));
        assertFalse(predicate1.test(expense2));

        // Multiple field
        FilterCommand cmd2 = parser.parse("t/jb t/food");
        Predicate<Expense> predicate2 = cmd2.getPredicate();
        assertTrue(predicate2.test(expense1));
        assertFalse(predicate2.test(expense2));
    }

    // Test filtering by payer
    @Test
    public void parsePayerFieldPresent() throws ParseException {
        // Single field
        FilterCommand cmd = parser.parse("p/john");
        Predicate<Expense> predicate1 = cmd.getPredicate();
        assertTrue(predicate1.test(expense1));
        assertFalse(predicate1.test(expense2));

        // Multiple field
        FilterCommand cmd2 = parser.parse("p/alice p/john");
        Predicate<Expense> predicate2 = cmd2.getPredicate();
        assertTrue(predicate2.test(expense1));
        assertTrue(predicate2.test(expense2));
    }

    // Test filtering by participants (prefix is s/)
    @Test
    public void parseParticipantsFieldPresent() throws ParseException {
        // Single field
        FilterCommand cmd = parser.parse("s/carrie");
        Predicate<Expense> predicate1 = cmd.getPredicate();
        assertFalse(predicate1.test(expense1));
        assertTrue(predicate1.test(expense2));

        // Multiple field
        FilterCommand cmd2 = parser.parse("s/jamie s/carrie");
        Predicate<Expense> predicate2 = cmd2.getPredicate();
        assertTrue(predicate2.test(expense1));
        assertTrue(predicate2.test(expense2));
    }
}
