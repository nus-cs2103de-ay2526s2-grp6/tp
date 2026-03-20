package fairshare.logic.commands;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;
import fairshare.model.expense.Expense;

public class DeleteCommand extends Command {
    private int expenseIndex;
    private static final String MESSAGE_SUCCESS = "Deleted expense: %s";
    private static final String MESSAGE_INVALID_INDEX = "Cannot delete an expense that is not in the list.";

    public DeleteCommand(int expenseIndex) {
        this.expenseIndex = expenseIndex;
    }

    public CommandResult execute(Model model) throws CommandException {
        try {
            Expense deletedExpense = model.deleteExpense(expenseIndex);
            return new CommandResult(String.format(MESSAGE_SUCCESS, deletedExpense.getExpenseName()));
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }
    }
}
