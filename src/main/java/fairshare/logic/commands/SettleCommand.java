package fairshare.logic.commands;

import fairshare.model.Model;
import fairshare.model.expense.Expense;

/**
 * Represents a command to settle balances from a person to another.
 * A settlement is an underlying expense that represents a debt repayment.
 */
public class SettleCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Settlement added successfully";
    private Expense settlement;

    /**
     * Creates an instance of {@code SettleCommand}.
     *
     * @param settlement The settlement (expense) to be added.
     */
    public SettleCommand(Expense settlement) {
        this.settlement = settlement;
    }

    /**
     * Executes the addition of the settlement ({@code Expense}) to the expense list.
     *
     * @param model The model the command should operate on.
     * @return A {@code CommandResult} containing the success message.
     */
    public CommandResult execute(Model model) {
        model.addExpense(settlement);
        return new CommandResult(MESSAGE_SUCCESS, false, false);
    }

}
