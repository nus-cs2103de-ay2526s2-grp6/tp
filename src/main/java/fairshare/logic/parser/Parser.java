package fairshare.logic.parser;

import fairshare.logic.commands.Command;
import fairshare.logic.parser.exceptions.ParseException;

/**
 * Represents a parser that parses user input into a {@code Command} for execution.
 */
public interface Parser {

    /**
     * Parses the specified user input string and returns the corresponding command.
     *
     * @param args The raw string of user-input arguments.
     * @return A {@code Command} object.
     * @throws ParseException If the user input does not match the required format.
     */
    Command parse(String args) throws ParseException;
}
