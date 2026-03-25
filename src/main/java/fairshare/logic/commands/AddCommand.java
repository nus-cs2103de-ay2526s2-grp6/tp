package fairshare.logic.commands;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;
import fairshare.model.expense.Expense;

public class AddCommand extends Command {
    private Expense expense;
    private static final String MESSAGE_SUCCESS = "New expense added: %s";

    public AddCommand(Expense expense) {
        this.expense = expense;
    }

    public CommandResult execute(Model model) {
        model.addExpense(expense);
        return new CommandResult(String.format(MESSAGE_SUCCESS, expense.getExpenseName()), false, false);
    }
}
