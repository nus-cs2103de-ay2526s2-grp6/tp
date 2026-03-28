package fairshare.logic.commands;

import fairshare.model.Model;

/**
 * Represents a command to terminate the program.
 */
public class ExitCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Shutting down FairShare...\nSee you soon!";

    /**
     * Executes the termination of the program.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     */
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_SUCCESS, false, true);
    }
}
