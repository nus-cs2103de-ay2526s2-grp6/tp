package fairshare.logic.commands;

import java.util.function.Predicate;

import fairshare.model.Model;
import fairshare.model.expense.Expense;

/**
 * Represents a command to filter expenses based on the given conditions.
 */
public class FilterCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Filter success.";
    private Predicate<Expense> predicate;

    /**
     * Creates an instance of {@code FilterCommand} and initializes it with a filter condition.
     *
     * @param predicate The condition that each expense must satisfy to be displayed.
     */
    public FilterCommand(Predicate<Expense> predicate) {
        this.predicate = predicate;
    }

    /**
     * Executes the filtering of the displayed expense list.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     */
    public CommandResult execute(Model model) {
        model.filterExpenses(predicate);
        return new CommandResult(MESSAGE_SUCCESS, false, false);
    }

    /**
     * Returns the predicate associated with this filter command.
     *
     * @return The {@code Predicate<Expense>} condition.
     */
    public Predicate<Expense> getPredicate() {
        return predicate;
    }
}
