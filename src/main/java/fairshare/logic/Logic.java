package fairshare.logic;

import java.util.List;

import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;
import javafx.collections.ObservableList;

/**
 * Represents the API of the logic component in FairShare
 */
public interface Logic {

    /**
     * Executes the given user input as a command.
     *
     * @param userInput the raw command string.
     * @return the {@code CommandResult} from executing the command.
     * @throws ParseException   if the input cannot be parsed.
     * @throws CommandException if the command execution fails.
     */
    CommandResult execute(String userInput)
            throws ParseException, CommandException;

    /**
     * Returns the filtered list of expenses from the model.
     *
     * @return an {@code ObservableList} of {@code Expense}.
     */
    ObservableList<Expense> getFilteredExpenseList();

    /**
     * Returns the list of balances calculated from all expenses.
     *
     * @return a list of {@code Balance} objects.
     */
    List<Balance> calculateBalances();

    /**
     * Returns the full unfiltered list of expenses.
     *
     * @return a {@code List} of all {@code Expense} objects.
     */
    List<Expense> getExpenseList();
}
