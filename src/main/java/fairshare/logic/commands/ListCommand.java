package fairshare.logic.commands;

import fairshare.model.Model;

public class ListCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Listing all expenses.";

    public CommandResult execute(Model model) {
        model.filterExpenses(expense -> true); // Matches all expenses
        return new CommandResult(MESSAGE_SUCCESS, false, false);
    }
}
