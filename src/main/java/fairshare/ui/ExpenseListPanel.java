package fairshare.ui;

import java.io.IOException;

import fairshare.model.expense.Expense;
import fairshare.ui.exceptions.UiException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * A UI panel that displays the list of expenses.
 * Each expense is rendered as an {@code ExpenseCard}.
 */
public class ExpenseListPanel {

    private static final String FXML = "/view/ExpenseListPanel.fxml";

    private Region root;

    @FXML
    private ListView<Expense> expenseListView;

    /**
     * Constructs an {@code ExpenseListPanel} with the given expense list.
     *
     * @param expenses the list of expenses to display; cannot be null.
     */
    public ExpenseListPanel(ObservableList<Expense> expenses) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    ExpenseListPanel.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        expenseListView.setItems(expenses);
        expenseListView.setCellFactory(lv -> new ExpenseListViewCell());
    }

    /**
     * Returns the root region of this component.
     *
     * @return the root {@code Region}.
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Refreshes the panel with an updated expense list.
     *
     * @param expenses the updated list of expenses; cannot be null.
     */
    public void refresh(ObservableList<Expense> expenses) {
        expenseListView.setItems(expenses);
    }

    /**
     * A custom {@code ListCell} that renders each {@code Expense}
     * as an {@code ExpenseCard}.
     */
    private static class ExpenseListViewCell extends ListCell<Expense> {

        /**
         * Updates the cell with the given expense.
         *
         * @param expense the expense to display.
         * @param isEmpty whether the cell is empty.
         */
        @Override
        protected void updateItem(Expense expense, boolean isEmpty) {
            super.updateItem(expense, isEmpty);

            if (isEmpty || expense == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ExpenseCard(
                        expense, getIndex() + 1).getRoot());
            }
        }
    }
}
