/**AI declaration --> claude ai was used to help complete this class,
 * namely how to load the different sections
 */

package fairshare.ui;

import java.io.IOException;

import fairshare.model.expense.Expense;
import fairshare.ui.exceptions.UiException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A UI panel that displays the list of expenses.
 * Each expense is rendered as an {@code ExpenseCard}.
 */
public class ExpenseListPanel {

    private static final String FXML = "/view/ExpenseListPanel.fxml";

    private VBox root;

    @FXML
    private ListView<Expense> expenseListView;

    @FXML
    private Label emptyStateLabel;

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

        updateEmptyState(expenses);
        expenses.addListener(
                (javafx.collections.ListChangeListener<Expense>) change ->
                        updateEmptyState(expenses));
    }

    /**
     * Returns the root region of this component.
     *
     * @return the root {@code VBox}.
     */
    public VBox getRoot() {
        return root;
    }

    /**
     * Refreshes the panel with an updated expense list.
     *
     * @param expenses the updated list of expenses; cannot be null.
     */
    public void refresh(ObservableList<Expense> expenses) {
        expenseListView.setItems(expenses);
        updateEmptyState(expenses);
    }

    /**
     * Shows or hides the empty state label based on whether
     * the expense list is empty.
     *
     * @param expenses the current list of expenses.
     */
    private void updateEmptyState(ObservableList<Expense> expenses) {
        boolean isEmpty = expenses.isEmpty();
        emptyStateLabel.setVisible(isEmpty);
        emptyStateLabel.setManaged(isEmpty);
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
                setStyle("-fx-background-color: transparent;");
                setOnMouseEntered(null);
                setOnMouseExited(null);
            } else {
                HBox card = new ExpenseCard(
                        expense, getIndex() + 1).getRoot();

                String baseStyle = card.getStyle();
                String hoverStyle = baseStyle
                        + "-fx-effect: dropshadow(gaussian,"
                        + "rgba(15,23,42,0.10), 22, 0, 0, 6);";

                card.setOnMouseEntered(e -> card.setStyle(hoverStyle));
                card.setOnMouseExited(e -> card.setStyle(baseStyle));

                setGraphic(card);
                setStyle("-fx-background-color: transparent;"
                        + "-fx-padding: 4 10 4 10;");
                setOnMouseEntered(null);
                setOnMouseExited(null);
            }
        }
    }
}
