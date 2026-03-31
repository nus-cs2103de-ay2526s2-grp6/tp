package fairshare.logic.commands;

import fairshare.model.Model;

/**
 * Represents a command remove all expenses from the expense list.
 */
public class ClearCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Expense list cleared successfully.";

    /**
     * Executes the removal of all {@code Expense} from the expense list.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     */
    public CommandResult execute(Model model) {
        model.clearExpenseList();
        return new CommandResult(MESSAGE_SUCCESS, false, false);
    }
}
