package fairshare.model;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import fairshare.model.balance.Balance;
import fairshare.model.balance.BalanceCalculator;
import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseList;
import fairshare.model.group.Group;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Represents the in-memory model of the FairShare data.
 */
public class ModelManager implements Model {
    public static final Predicate<Expense> LIST_ALL_EXPENSES = expense -> true;
    private final ExpenseList expenseList;
    private final FilteredList<Expense> filteredExpenses;

    /**
     * Creates an instance of {@code ModelManager} with an empty underlying expense list.
     */
    public ModelManager() {
        this.expenseList = new ExpenseList();
        this.filteredExpenses = new FilteredList<>(this.expenseList.getUnmodifiableExpenseList(), LIST_ALL_EXPENSES);
    }

    /**
     * Creates an instance of {@code ModelManager} with the specified list of expenses.
     *
     * @param expenseList The initial list of {@code Expense} objects to load into the model.
     */
    public ModelManager(List<Expense> expenseList) {
        assert expenseList != null : "expenseList should not be null";

        this.expenseList = new ExpenseList(expenseList);
        this.filteredExpenses = new FilteredList<>(this.expenseList.getUnmodifiableExpenseList(), LIST_ALL_EXPENSES);
    }

    @Override
    public void addExpense(Expense expense) {
        assert expense != null : "expense should not be null";

        expenseList.addExpense(expense);
    }

    @Override
    public Expense deleteExpense(Expense expense) {
        assert expense != null : "expense should not be null";

        return expenseList.deleteExpense(expense);
    }

    @Override
    public void updateExpense(Expense targetExpense, Expense updatedExpense) {
        assert targetExpense != null && updatedExpense != null : "targetExpense and updatedExpense should not be null";

        expenseList.updateExpense(targetExpense, updatedExpense);
    }

    @Override
    public void filterExpenses(Predicate<Expense> predicate) {
        assert predicate != null : "predicate should not be null";

        filteredExpenses.setPredicate(predicate);
    }

    @Override
    public void clearExpenseList() {
        expenseList.clearExpenseList();
    }

    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return filteredExpenses;
    }

    @Override
    public List<Expense> getExpenseList() {
        return expenseList.getExpenseList();
    }

    @Override
    public Map<Group, List<Balance>> calculateBalances() {
        return BalanceCalculator.calculate(filteredExpenses);
    }

    @Override
    public Map<Group, List<Balance>> calculateAllBalances() {
        return BalanceCalculator.calculate(
                expenseList.getUnmodifiableExpenseList());
    }
}
