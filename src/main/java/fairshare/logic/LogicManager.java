package fairshare.logic;

import fairshare.logic.commands.Command;
import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.FairShareParser;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.Model;

public class LogicManager implements Logic {
    private FairShareParser fairShareParser;
    private Model model;
    // private Storage storage;

    public LogicManager(Model model) {
        this.fairShareParser = new FairShareParser();
        this.model = model;
    }

    public CommandResult execute(String userInput) throws ParseException, CommandException{
        Command cmd = fairShareParser.parseCommand(userInput);
        CommandResult cmdResult = cmd.execute(model);

        // storage.saveExpenseList

        return cmdResult;
    }
}
