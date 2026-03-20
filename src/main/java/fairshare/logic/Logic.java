package fairshare.logic;

import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;

public interface Logic {
    public CommandResult execute(String userInput) throws ParseException, CommandException;
}
