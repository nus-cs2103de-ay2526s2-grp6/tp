package fairshare.logic.commands;

import fairshare.model.Model;

/**
 * Represents a command to list all expenses in the list.
 */
public class ListCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Listing all expenses.";

    /**
     * Executes the displaying of all current expenses in the expense list.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     */
    public CommandResult execute(Model model) {
        model.filterExpenses(expense -> true); // Matches all expenses
        return new CommandResult(MESSAGE_SUCCESS, false, false);
    }
}
