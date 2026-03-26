package fairshare.logic.parser;

import fairshare.logic.commands.DeleteCommand;
import fairshare.logic.parser.exceptions.ParseException;

public class DeleteCommandParser implements Parser {

    public DeleteCommand parse(String args) throws ParseException {
        int expenseId = ParserUtil.parseExpenseId(args);
        int expenseIndex = expenseId - 1;
        return new DeleteCommand(expenseIndex);
    }
}
