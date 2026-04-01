package fairshare.model.expense;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a list of expense in the shared expense tracker (FairShare)
 */
public class ExpenseList {
    private final ObservableList<Expense> expenses;
    private final ObservableList<Expense> unmodifiableExpenses;

    /**
     * Creates an empty {@code ExpenseList}.
     */
    public ExpenseList() {
        this.expenses = FXCollections.observableArrayList();
        this.unmodifiableExpenses = FXCollections.unmodifiableObservableList(expenses);
    }

    /**
     * Creates an {@code ExpenseList} with the given list of expenses.
     *
     * @param expenses A list of {@code Expense} objects.
     */
    public ExpenseList(List<Expense> expenses) {
        this.expenses = FXCollections.observableArrayList(expenses);
        this.unmodifiableExpenses = FXCollections.unmodifiableObservableList(this.expenses);
    }

    /**
     * Adds an expense to the expense list.
     *
     * @param expense The {@code Expense} to be added.
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    /**
     * Removes the specified expense from the expense list.
     *
     * @param expense The exact {@code Expense} to be removed.
     * @return The removed {@code Expense}.
     */
    public Expense deleteExpense(Expense expense) {
        int expenseIndex = getExpenseIndex(expense);
        return expenses.remove(expenseIndex);
    }

    /**
     * Updates a specific expense in the expense list by replacing it with an updated expense.
     *
     * @param targetExpense The original {@code Expense} to be replaced.
     * @param updatedExpense The updated {@code Expense}.
     */
    public void updateExpense(Expense targetExpense, Expense updatedExpense) {
        int targetExpenseIndex = getExpenseIndex(targetExpense);
        expenses.set(targetExpenseIndex, updatedExpense);
    }

    /**
     * Removes all expenses from the expense list.
     */
    public void clearExpenseList() {
        expenses.clear();
    }

    /**
     * Returns the mutable list of expenses.
     *
     * @return A {@code List} containing the current expenses.
     */
    public List<Expense> getExpenseList() {
        return this.expenses;
    }

    /**
     * Returns an unmodifiable view of the expense list.
     *
     * @return An unmodifiable {@code ObservableList} of expenses.
     */
    public ObservableList<Expense> getUnmodifiableExpenseList() {
        return unmodifiableExpenses;
    }

    private int getExpenseIndex(Expense expense) {
        int expenseIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            // Compare using mem address to allow duplicate expense (same name, payer, etc.)
            if (expense == expenses.get(i)) {
                expenseIndex = i;
                break;
            }
        }
        return expenseIndex;
    }
}
