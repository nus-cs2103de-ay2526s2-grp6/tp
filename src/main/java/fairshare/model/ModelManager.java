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

    public ModelManager(List<Expense> expenseList) {
        this.expenseList = new ExpenseList(expenseList);
        this.filteredExpenses = new FilteredList<>(this.expenseList.getUnmodifiableExpenseList(), LIST_ALL_EXPENSES);
    }

    public void addExpense(Expense expense) {
        expenseList.addExpense(expense);
    }

    public Expense deleteExpense(Expense expense) {
        return expenseList.deleteExpense(expense);
    }

    public void updateExpense(Expense targetExpense, Expense updatedExpense) {
        expenseList.updateExpense(targetExpense, updatedExpense);
    }

    public void filterExpenses(Predicate<Expense> predicate) {
        filteredExpenses.setPredicate(predicate);
    }

    public ObservableList<Expense> getFilteredExpenseList() {
        return filteredExpenses;
    }

    public List<Expense> getExpenseList() {
        return expenseList.getExpenseList();
    }

    public List<Balance> calculateBalances() {
        return BalanceCalculator.calculate(filteredExpenses);
    }
}
