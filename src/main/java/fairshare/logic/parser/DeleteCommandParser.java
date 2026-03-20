package fairshare.logic.parser;

import fairshare.logic.commands.DeleteCommand;
import fairshare.logic.parser.exceptions.ParseException;

public class DeleteCommandParser implements Parser {

    public DeleteCommand parse(String args) throws ParseException {
        int expenseId = parseExpenseId(args);
        int expenseIndex = expenseId - 1;
        return new DeleteCommand(expenseIndex);
    }

    private int parseExpenseId(String expenseId) throws ParseException {
        try {
            return Integer.parseInt(expenseId);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid expense id: " + expenseId);
        }
    }
}
