package fairshare.logic.commands;

import java.util.function.Predicate;

import fairshare.model.Model;
import fairshare.model.expense.Expense;

/**
 * Represents a command to filter expenses based on the given conditions.
 */
public class FilterCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Filter success. Displaying %d expenses.";
    private static final String MESSAGE_EMPTY = "No expenses found.";
    private Predicate<Expense> predicate;

    /**
     * Creates an instance of {@code FilterCommand} and initializes it with a filter condition.
     *
     * @param predicate The condition that each expense must satisfy to be displayed.
     */
    public FilterCommand(Predicate<Expense> predicate) {
        assert predicate != null : "predicate should not be null";

        this.predicate = predicate;
    }

    /**
     * Executes the filtering of the displayed expense list.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     */
    public CommandResult execute(Model model) {
        assert model != null : "model should not be null";

        model.filterExpenses(predicate);
        int filteredListSize = model.getFilteredExpenseList().size();

        if (filteredListSize == 0) {
            return new CommandResult(MESSAGE_EMPTY, false, false);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, filteredListSize), false, false);
    }

    /**
     * Returns the predicate associated with this filter command.
     *
     * @return The {@code Predicate<Expense>} condition.
     */
    public Predicate<Expense> getPredicate() {
        return this.predicate;
    }
}
