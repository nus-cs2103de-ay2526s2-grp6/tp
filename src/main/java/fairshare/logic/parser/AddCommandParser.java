package fairshare.logic.parser;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import fairshare.logic.commands.AddCommand;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.model.expense.Participant;
import fairshare.model.group.Group;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object.
 */
public class AddCommandParser implements Parser {

    /**
     * Parses the given {@code String} of arguments in the context of AddCommand.
     *
     * @param args The raw string of user-input arguments.
     * @return An {@code AddCommand} object.
     * @throws ParseException If the user input does not match the required format.
     */
    public AddCommand parse(String args) throws ParseException {
        Map<String, List<String>> map = ParserUtil.tokenize(args);

        Group group = new Group(ParserUtil.getSingleFieldData(map, "g"));
        String expenseName = ParserUtil.getSingleFieldData(map, "n");
        double amount = ParserUtil.parseAmount(ParserUtil.getSingleFieldData(map, "a"));
        Person payer = new Person(ParserUtil.getSingleFieldData(map, "p"));

        List<String> strParticipants = ParserUtil.getMultiFieldData(map, "s");
        List<Participant> participants = ParserUtil.parseParticipants(strParticipants);

        Optional<List<String>> strTags = ParserUtil.getOptionalMultiFieldData(map, "t");
        List<Tag> tags;
        if (strTags.isPresent()) {
            tags = ParserUtil.parseTags(strTags.get());
        } else {
            tags = List.of();
        }

        Expense expense = new Expense(group, expenseName, amount, payer, participants, tags, ExpenseType.EXPENSE);
        return new AddCommand(expense);
    }
}
