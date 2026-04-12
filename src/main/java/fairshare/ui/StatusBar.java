package fairshare.ui;

import java.io.IOException;
import java.util.List;

import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * A UI component that displays a summary of the current expense list.
 */
public class StatusBar {

    private static final String FXML = "/view/StatusBar.fxml";

    private HBox root;

    @FXML
    private Label totalExpensesLabel;

    @FXML
    private Label totalAmountLabel;

    /**
     * Constructs a {@code StatusBar} with the given list of expenses.
     *
     * @param expenses the list of expenses;
     */
    public StatusBar(List<Expense> expenses) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    StatusBar.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        refresh(expenses);
    }

    /**
     * Returns the root node of this component.
     *
     * @return the root {@code HBox}.
     */
    public HBox getRoot() {
        return root;
    }

    /**
     * Refreshes the status bar with updated expense data.
     *
     * @param expenses the updated list of expenses;
     */
    public void refresh(List<Expense> expenses) {
        long count = expenses.stream()
                .filter(e -> e.getExpenseType() == ExpenseType.EXPENSE)
                .count();

        double total = expenses.stream()
                .filter(e -> e.getExpenseType() == ExpenseType.EXPENSE)
                .mapToDouble(Expense::getAmount)
                .sum();

        totalExpensesLabel.setText("Total Expenses: " + count);
        totalAmountLabel.setText(String.format("Grand Total: $%.2f", total));
    }
}
