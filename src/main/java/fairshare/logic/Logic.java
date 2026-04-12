package fairshare.logic;

import java.util.List;
import java.util.Map;

import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;
import fairshare.model.group.Group;
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
     * Calculates and returns the simplified list of debts, grouped by their respective groups.
     *
     * @return A map where each key is a {@code Group} and the corresponding value is a list of {@code Balance}
     *     objects representing the balances within that specific group.
     */
    Map<Group, List<Balance>> calculateBalances();

    /**
     * Returns the full unfiltered list of expenses.
     *
     * @return a {@code List} of all {@code Expense} objects.
     */
    List<Expense> getExpenseList();

    /**
     * Calculates and returns the simplified list of debts for all
     * expenses regardless of current filter, grouped by group.
     *
     * @return a map of group to list of balances using full expense list.
     */
    Map<Group, List<Balance>> calculateAllBalances();
}
