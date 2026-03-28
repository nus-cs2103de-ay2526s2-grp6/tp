package fairshare.logic.commands;

import fairshare.model.Model;

/**
 * Represents a command to display the list of available commands and their syntaxes.
 */
public class HelpCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Displaying help window";

    /**
     * Executes the help command by displaying a help window describing all supported operations.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     */
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_SUCCESS, true, false);
    }
}
