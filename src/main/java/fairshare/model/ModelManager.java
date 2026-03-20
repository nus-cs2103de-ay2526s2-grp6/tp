package fairshare.model;

import fairshare.model.balance.Balance;
import fairshare.model.balance.BalanceCalculator;
import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseList;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.List;
import java.util.function.Predicate;

public class ModelManager implements Model {
    private final ExpenseList expenseList;
    private final FilteredList<Expense> filteredExpenses;
    public static final Predicate<Expense> LIST_ALL_EXPENSES = expense -> true;

    public ModelManager() {
        this.expenseList = new ExpenseList();
        this.filteredExpenses = new FilteredList<>(this.expenseList.getUnmodifiableExpenseList(), LIST_ALL_EXPENSES);
    }

    public void addExpense(Expense expense) {
        expenseList.addExpense(expense);
    }

    public Expense deleteExpense(int index) {
        return expenseList.deleteExpense(index);
    }

    public void filterExpenses(Predicate<Expense> predicate) {
        filteredExpenses.setPredicate(predicate);
    }

    public ObservableList<Expense> getFilteredExpenseList() {
        return filteredExpenses;
    }

    public List<Balance> calculateBalances() {
        return BalanceCalculator.calculate(expenseList.getExpenseList());
    }
}
