package fairshare.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import fairshare.logic.commands.FilterCommand;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Expense;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FilterCommandParserTest {
    private FilterCommandParser parser;
    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    public void setUp() {
        parser = new FilterCommandParser();

        String expense1Name = "lunch";
        double expense1Amt = 25.0d;
        Person expense1Payer = new Person("john");
        List<Person> expense1Participants = new ArrayList<>(
                List.of(expense1Payer, new Person("alice"), new Person("jamie")));
        List<Tag> expense1Tags = new ArrayList<>(List.of(new Tag("food"), new Tag("jb")));
        expense1 = new Expense(expense1Name, expense1Amt, expense1Payer, expense1Participants, expense1Tags);

        String expense2Name = "dinner";
        double expense2Amt = 54.0d;
        Person expense2Payer = new Person("john");
        List<Person> expense2Participants = new ArrayList<>(
                List.of(new Person("alice"), new Person("jamie"), new Person("carrie")));
        List<Tag> expense2Tags = new ArrayList<>(List.of(new Tag("food"), new Tag("jb")));
        expense2 = new Expense(expense2Name, expense2Amt, expense2Payer, expense2Participants, expense2Tags);
    }

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
}
