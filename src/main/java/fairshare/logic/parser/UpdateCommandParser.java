package fairshare.logic.parser;

import fairshare.logic.commands.UpdateCommand;
import fairshare.logic.commands.UpdateCommand.UpdateFields;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UpdateCommandParser implements Parser {
    public UpdateCommand parse(String args) throws ParseException {
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

    private UpdateFields createUpdateFields(Map<String, List<String>> map) throws ParseException{
        Optional<String> expenseName = ParserUtil.getOptionalSingleFieldData(map, "n");
        Optional<String> strAmount = ParserUtil.getOptionalSingleFieldData(map, "a");
        Optional<String> strPayer = ParserUtil.getOptionalSingleFieldData(map, "p");
        Optional<List<String>> strParticipants = ParserUtil.getOptionalMultiFieldData(map, "s");
        Optional<List<String>> strTags = ParserUtil.getOptionalMultiFieldData(map, "t");

        UpdateFields updateFields = new UpdateFields();

        if (expenseName.isPresent()) {
            updateFields.setExpenseName(expenseName.get());
        }
        if (strAmount.isPresent()) {
            double amount = ParserUtil.parseAmount(strAmount.get());
            updateFields.setAmount(amount);
        }
        if (strPayer.isPresent()) {
            updateFields.setPayer(new Person(strPayer.get()));
        }
        if (strParticipants.isPresent()) {
            List<Person> participants = ParserUtil.parseParticipants(strParticipants.get());
            updateFields.setParticipants(participants);
        }
        if (strTags.isPresent()) {
            List<Tag> tags = ParserUtil.parseTags(strTags.get());
            updateFields.setTags(tags);
        }

        return updateFields;
    }
}
