package fairshare.model.expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseList {
    private final List<Expense> expenses;

    public ExpenseList() {
        this.expenses = new ArrayList<>();
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
}
