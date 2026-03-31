package fairshare.logic.parser;

import java.util.List;
import java.util.Map;

import fairshare.logic.commands.SettleCommand;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Expense;
import fairshare.model.group.Group;
import fairshare.model.person.Person;

/**
 * Parses input arguments and creates a new SettleCommand object.
 */
public class SettleCommandParser implements Parser {

    /**
     * Parses the given {@code String} of arguments in the context of SettleCommand.
     *
     * @param args The raw string of user-input arguments.
     * @return An {@code SettleCommand} object.
     * @throws ParseException If the user input does not match the required format.
     */
    public SettleCommand parse(String args) throws ParseException {
        Map<String, List<String>> map = ParserUtil.tokenize(args);

        Group group = new Group(ParserUtil.getSingleFieldData(map, "g"));
        Person payer = new Person(ParserUtil.getSingleFieldData(map, "p"));
        Person receiver = new Person(ParserUtil.getSingleFieldData(map, "r"));
        double amount = ParserUtil.parseAmount(ParserUtil.getSingleFieldData(map, "a"));

        Expense settlement = Expense.createSettlement(group, payer, receiver, amount);
        return new SettleCommand(settlement);
    }
}
