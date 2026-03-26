package fairshare.logic.commands;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;
import fairshare.model.expense.Expense;

import java.util.List;

public class DeleteCommand extends Command {
    private int expenseIndex;
    private static final String MESSAGE_SUCCESS = "Deleted expense: %s";
    private static final String MESSAGE_INVALID_INDEX = "Cannot delete an expense that is not in the list.";

    public DeleteCommand(int expenseIndex) {
        this.expenseIndex = expenseIndex;
    }

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
