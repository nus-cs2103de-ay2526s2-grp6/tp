package fairshare.logic.parser;

import java.util.List;
import java.util.Map;

import fairshare.logic.commands.AddCommand;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Expense;
import fairshare.model.expense.Participant;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

public class AddCommandParser implements Parser {

    public AddCommand parse(String args) throws ParseException {
        Map<String, List<String>> map = ParserUtil.tokenize(args);

        String expenseName = ParserUtil.getSingleFieldData(map, "n");
        double amount = ParserUtil.parseAmount(ParserUtil.getSingleFieldData(map, "a"));
        Person payer = new Person(ParserUtil.getSingleFieldData(map, "p"));

        List<String> strParticipants = ParserUtil.getMultiFieldData(map, "s");
        List<Participant> participants = ParserUtil.parseParticipants(strParticipants);

        List<String> strTags = ParserUtil.getMultiFieldData(map, "t");
        List<Tag> tags = ParserUtil.parseTags(strTags);

        Expense expense = new Expense(expenseName, amount, payer, participants, tags);
        return new AddCommand(expense);
    }
}
