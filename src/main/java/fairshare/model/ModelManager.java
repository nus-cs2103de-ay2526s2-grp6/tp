package fairshare.model;

import fairshare.model.balance.Balance;
import fairshare.model.balance.BalanceCalculator;
import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseList;

import java.util.List;

public class ModelManager implements Model {
    private final ExpenseList expenseList;

    public ModelManager() {
        this.expenseList = new ExpenseList();
    }

    public void addExpense(Expense expense) {
        expenseList.addExpense(expense);
    }

    public void deleteExpense(int index) {
        expenseList.deleteExpense(index);
    }

    public List<Balance> getSimplifiedBalances() {
        return BalanceCalculator.calculate(expenseList.getExpenseList());
    }
}
