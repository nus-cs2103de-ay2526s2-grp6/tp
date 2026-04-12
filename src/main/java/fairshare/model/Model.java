package fairshare.model;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;
import fairshare.model.group.Group;
import javafx.collections.ObservableList;

/**
 * Represents the API of the model component in FairShare
 */
public interface Model {

    /**
     * Adds the given expense to the tracker.
     *
     * @param expense The {@code Expense} to be added.
     */
    void addExpense(Expense expense);

    /**
     * Deletes the specified expense from the tracker.
     *
     * @param expense The exact {@code Expense} to be deleted.
     * @return The {@code Expense} that was successfully removed.
     */
    Expense deleteExpense(Expense expense);

    /**
     * Updates the specified target expense by replacing it with an updated expense.
     *
     * @param targetExpense The original {@code Expense} to be updated.
     * @param updatedExpense The new {@code Expense} containing the updated fields.
     */
    void updateExpense(Expense targetExpense, Expense updatedExpense);

    /**
     * Updates the filter of the filtered expense list to display only expenses matching the given predicate.
     *
     * @param predicate The condition that an expense must satisfy to be displayed.
     */
    void filterExpenses(Predicate<Expense> predicate);

    /**
     * Removes all {@code Expense} from the expense list.
     */
    void clearExpenseList();

    /**
     * Returns an unmodifiable observable view of the filtered expense list.
     *
     * @return An {@code ObservableList} containing the filtered expenses.
     */
    ObservableList<Expense> getFilteredExpenseList();

    /**
     * Returns the raw list of all expenses currently in the tracker.
     *
     * @return A {@code List} of all {@code Expense} objects.
     */
    List<Expense> getExpenseList();

    /**
     * Calculates and returns the simplified list of debts, grouped by their respective groups.
     *
     * @return A map where each key is a {@code Group} and the corresponding value is a list of {@code Balance}
     *     objects representing the balances within that specific group.
     */
    Map<Group, List<Balance>> calculateBalances();

    /**
     * Calculates and returns the simplified list of debts for all
     * expenses regardless of current filter, grouped by group.
     *
     * @return a map of group to list of balances using full expense list.
     */
    Map<Group, List<Balance>> calculateAllBalances();
}
