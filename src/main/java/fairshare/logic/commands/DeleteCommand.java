package fairshare.logic.commands;

import java.util.List;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;
import fairshare.model.expense.Expense;

/**
 * Represents a command to remove an expense from the list.
 */
public class DeleteCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Deleted expense: %s";
    private static final String MESSAGE_INVALID_INDEX = "Cannot delete an expense that is not in the list.";
    private int expenseIndex;

    /**
     * Creates an instance of {@code DeleteCommand} and initializes it with the given expense index.
     *
     * @param expenseIndex The zero-based index of the expense to be deleted, based on the currently displayed list.
     */
    public DeleteCommand(int expenseIndex) {
        this.expenseIndex = expenseIndex;
    }

    /**
     * Executes the removal of the {@code Expense} from the expense list.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     * @throws CommandException If the provided index is out of bounds of the currently displayed list.
     */
    public CommandResult execute(Model model) throws CommandException {
        try {
            List<Expense> displayedExpenseList = model.getFilteredExpenseList();
            Expense expenseToDelete = displayedExpenseList.get(expenseIndex);
            model.deleteExpense(expenseToDelete);
            return new CommandResult(String.format(MESSAGE_SUCCESS, expenseToDelete.getExpenseName()),
                    false, false);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }
    }
}
