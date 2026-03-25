package fairshare.logic.commands;

import fairshare.model.Model;

public class HelpCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Displaying help window";

    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_SUCCESS, true, false);
    }
}
