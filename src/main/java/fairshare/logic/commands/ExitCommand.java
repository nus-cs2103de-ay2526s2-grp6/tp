package fairshare.logic.commands;

import fairshare.model.Model;

public class ExitCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Shutting down FairShare...\nSee you soon!";

    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_SUCCESS, false, true);
    }
}
