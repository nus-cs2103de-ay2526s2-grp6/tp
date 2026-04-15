package fairshare.logic.parser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import fairshare.logic.commands.UpdateCommand;
import fairshare.logic.commands.UpdateCommand.UpdateFields;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Participant;
import fairshare.model.group.Group;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

/**
 * Parses input arguments and creates a new UpdateCommand object.
 */
public class UpdateCommandParser implements Parser {

    /**
     * Parses the given {@code String} of arguments in the context of UpdateCommand.
     *
     * @param args The raw string of user-input arguments.
     * @return An {@code UpdateCommand} object.
     * @throws ParseException If the user input does not match the required format.
     */
    public UpdateCommand parse(String args) throws ParseException {
        assert args != null : "args should not be null";

        String[] updateParts = args.split("\\s+", 2);
        if (updateParts.length == 1) {
            throw new ParseException("Please specify the fields to update.");
        }

        int expenseId = ParserUtil.parseExpenseId(updateParts[0]);
        int expenseIndex = expenseId - 1;

        Map<String, List<String>> map = ParserUtil.tokenize(updateParts[1]);

        UpdateFields updateFields = createUpdateFields(map);
        if (updateFields.isEmpty()) { // Covers non-empty arg but invalid flag e.g., z/test
            throw new ParseException("Please specify at least one valid field to update.");
        }

        return new UpdateCommand(expenseIndex, updateFields);
    }

    private UpdateFields createUpdateFields(Map<String, List<String>> map) throws ParseException {
        UpdateFields updateFields = new UpdateFields();

        setGroup(map, updateFields);
        setExpenseName(map, updateFields);
        setAmount(map, updateFields);
        setPayer(map, updateFields);
        setParticipants(map, updateFields);
        setTags(map, updateFields);
        setReceiver(map, updateFields);

        return updateFields;
    }

    private void setGroup(Map<String, List<String>> map, UpdateFields updateFields) throws ParseException {
        Optional<String> group = ParserUtil.getOptionalSingleFieldData(map, "g");
        if (group.isPresent()) {
            updateFields.setGroup(new Group(group.get()));
        }
    }

    private void setExpenseName(Map<String, List<String>> map, UpdateFields updateFields) throws ParseException {
        Optional<String> expenseName = ParserUtil.getOptionalSingleFieldData(map, "n");
        if (expenseName.isPresent()) {
            updateFields.setExpenseName(expenseName.get());
        }
    }

    private void setAmount(Map<String, List<String>> map, UpdateFields updateFields) throws ParseException {
        Optional<String> strAmount = ParserUtil.getOptionalSingleFieldData(map, "a");
        if (strAmount.isPresent()) {
            double amount = ParserUtil.parseAmount(strAmount.get());
            updateFields.setAmount(amount);
        }
    }

    private void setPayer(Map<String, List<String>> map, UpdateFields updateFields) throws ParseException {
        Optional<String> strPayer = ParserUtil.getOptionalSingleFieldData(map, "p");
        if (strPayer.isPresent()) {
            updateFields.setPayer(new Person(strPayer.get()));
        }
    }

    private void setParticipants(Map<String, List<String>> map, UpdateFields updateFields) throws ParseException {
        Optional<List<String>> strParticipants = ParserUtil.getOptionalMultiFieldData(map, "s");
        if (strParticipants.isPresent()) {
            Set<Participant> participants = ParserUtil.parseParticipants(strParticipants.get());
            updateFields.setParticipants(participants);
        }
    }

    private void setTags(Map<String, List<String>> map, UpdateFields updateFields) throws ParseException {
        Optional<List<String>> strTags = ParserUtil.getOptionalMultiFieldData(map, "t");
        if (strTags.isPresent()) {
            Set<Tag> tags = ParserUtil.parseTags(strTags.get());
            updateFields.setTags(tags);
        }
    }

    private void setReceiver(Map<String, List<String>> map, UpdateFields updateFields) throws ParseException {
        Optional<String> strReceiver = ParserUtil.getOptionalSingleFieldData(map, "r");
        if (strReceiver.isPresent()) {
            updateFields.setReceiver(new Participant(new Person(strReceiver.get()), 1));
        }
    }
}
