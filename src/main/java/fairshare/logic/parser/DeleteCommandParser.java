package fairshare.logic.parser;

import fairshare.logic.commands.DeleteCommand;
import fairshare.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser {

    /**
     * Parses the given {@code String} of arguments in the context of DeleteCommand.
     *
     * @param args The raw string of user-input arguments.
     * @return A {@code DeleteCommand} object.
     * @throws ParseException If the user input does not match the required format.
     */
    public DeleteCommand parse(String args) throws ParseException {
        int expenseId = ParserUtil.parseExpenseId(args);
        int expenseIndex = expenseId - 1;
        return new DeleteCommand(expenseIndex);
    }
}
