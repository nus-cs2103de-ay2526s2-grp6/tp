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

    public Expense deleteExpense(Expense expense) {
        int expenseIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            if (expense == expenses.get(i)) {
                expenseIndex = i;
                break;
            }
        }
        return expenses.remove(expenseIndex);
    }

    public void updateExpense(Expense targetExpense, Expense updatedExpense) {
        int expenseIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            // Compare using mem address to allow duplicate expense (same name, payer, etc.)
            if (targetExpense == expenses.get(i)) {
                expenseIndex = i;
                break;
            }
        }
        expenses.set(expenseIndex, updatedExpense);
    }

    public List<Expense> getExpenseList() {
        return this.expenses;
    }

    public ObservableList<Expense> getUnmodifiableExpenseList() {
        return unmodifiableExpenses;
    }
}
