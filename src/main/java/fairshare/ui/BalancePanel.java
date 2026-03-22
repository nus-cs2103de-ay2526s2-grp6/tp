package fairshare.ui;

import java.io.IOException;
import java.util.List;

import fairshare.model.balance.Balance;
import fairshare.ui.exceptions.UiException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * A UI panel that displays the net balance summary,
 * showing who owes whom and how much.
 */
public class BalancePanel {

    private static final String FXML = "/view/BalancePanel.fxml";

    private Region root;

    @FXML
    private ListView<Balance> balanceListView;

    /**
     * Constructs a {@code BalancePanel} with the given list of balances.
     *
     * @param balances the initial list of balances to display; cannot be null.
     */
    public BalancePanel(List<Balance> balances) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    BalancePanel.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        balanceListView.setItems(
                FXCollections.observableArrayList(balances));
        balanceListView.setCellFactory(lv -> new BalanceListViewCell());
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
     * Refreshes the panel with an updated list of balances.
     *
     * @param balances the updated list of balances; cannot be null.
     */
    public void refresh(List<Balance> balances) {
        balanceListView.setItems(
                FXCollections.observableArrayList(balances));
    }

    /**
     * A custom {@code ListCell} that renders each {@code Balance}
     * as a formatted text entry.
     */
    private static class BalanceListViewCell extends ListCell<Balance> {

        /**
         * Updates the cell with the given balance entry.
         *
         * @param balance the balance to display.
         * @param isEmpty whether the cell is empty.
         */
        @Override
        protected void updateItem(Balance balance, boolean isEmpty) {
            super.updateItem(balance, isEmpty);

            if (isEmpty || balance == null) {
                setGraphic(null);
                setText(null);
            } else {
                setText(balance.getDebtor().getName()
                        + " owes "
                        + balance.getCreditor().getName()
                        + " $"
                        + String.format("%.2f", balance.getAmount()));
            }
        }
    }
}
