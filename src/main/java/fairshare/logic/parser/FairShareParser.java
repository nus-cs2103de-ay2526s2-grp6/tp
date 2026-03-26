package fairshare.logic.parser;

import fairshare.logic.commands.Command;
import fairshare.logic.commands.ExitCommand;
import fairshare.logic.commands.HelpCommand;
import fairshare.logic.commands.ListCommand;
import fairshare.logic.parser.exceptions.ParseException;

public class FairShareParser {

    public Command parseCommand(String userInput) throws ParseException {
        String[] parts = userInput.trim().split("\\s+", 2);

        String cmd = parts[0];
        String args = (parts.length == 2) ? parts[1] : "";

        switch(cmd) {
        case "add":
            return new AddCommandParser().parse(args);
        case "delete":
            return new DeleteCommandParser().parse(args);
        case "filter":
            return new FilterCommandParser().parse(args);
        case "update":
            return new UpdateCommandParser().parse(args);
        case "list":
            return new ListCommand();
        case "help":
            return new HelpCommand();
        case "exit":
            return new ExitCommand();
        default:
            throw new ParseException("Invalid command: " + cmd
                    + "\nType \"help\" to view a list of available commands.");
        }
    }
}
