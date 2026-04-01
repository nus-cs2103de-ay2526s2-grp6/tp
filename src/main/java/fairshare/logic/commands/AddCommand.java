package fairshare.logic.commands;

import fairshare.model.Model;
import fairshare.model.expense.Expense;

/**
 * Represents a command to add a new expense to the list.
 */
public class AddCommand extends Command {
    private static final String MESSAGE_SUCCESS = "New expense added: %s";
    private Expense expense;

    /**
     * Creates an instance of {@code AddCommand} and initializes it with the given expense.
     *
     * @param expense The expense to be added.
     */
    public AddCommand(Expense expense) {
        assert expense != null : "expense should not be null";

        this.expense = expense;
    }

    /**
     * Executes the addition of the {@code Expense} to the expense list.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     */
    public CommandResult execute(Model model) {
        assert model != null : "model should not be null";

        model.addExpense(expense);
        return new CommandResult(String.format(MESSAGE_SUCCESS, expense.getExpenseName()), false, false);
    }
}
