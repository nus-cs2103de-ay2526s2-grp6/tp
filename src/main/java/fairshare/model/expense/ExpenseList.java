package fairshare.model.expense;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ExpenseList {
    private final ObservableList<Expense> expenses;
    private final ObservableList<Expense> unmodifiableExpenses;

    public ExpenseList() {
        this.expenses = FXCollections.observableArrayList();
        this.unmodifiableExpenses = FXCollections.unmodifiableObservableList(expenses);
    }

    public ExpenseList(List<Expense> expenses) {
        this.expenses = FXCollections.observableArrayList(expenses);
        this.unmodifiableExpenses = FXCollections.unmodifiableObservableList(this.expenses);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public Expense deleteExpense(int expenseIndex) {
        return expenses.remove(expenseIndex);
    }

    public List<Expense> getExpenseList() {
        return this.expenses;
    }

    public ObservableList<Expense> getUnmodifiableExpenseList() {
        return unmodifiableExpenses;
    }
}
