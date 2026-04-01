package fairshare.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private ListView<Map.Entry<String, List<Balance>>> balanceListView;

    /**
     * Constructs a {@code BalancePanel} with the given list of balances.
     *
     * @param balances the initial list of balances to display;
     *                 cannot be null.
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

        setBalances(balances);
        balanceListView.setCellFactory(
                lv -> new BalanceListViewCell());
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
        setBalances(balances);
    }

    /**
     * Groups balances by debtor name and sets them on the list view.
     *
     * @param balances the list of balances to group and display.
     */
    private void setBalances(List<Balance> balances) {
        Map<String, List<Balance>> grouped = new LinkedHashMap<>();

        for (Balance balance : balances) {
            String debtorName = balance.getDebtor().getName();
            grouped.computeIfAbsent(debtorName,
                    k -> new ArrayList<>()).add(balance);
        }

        balanceListView.setItems(
                FXCollections.observableArrayList(
                        grouped.entrySet()));
    }

    /**
     * A custom {@code ListCell} that renders each person's balances
     * as a {@code BalanceCard}.
     */
    private static class BalanceListViewCell
            extends ListCell<Map.Entry<String, List<Balance>>> {

        /**
         * Updates the cell with the given balance entry.
         *
         * @param entry   the map entry containing person name and
         *                their list of balances.
         * @param isEmpty whether the cell is empty.
         */
        @Override
        protected void updateItem(
                Map.Entry<String, List<Balance>> entry,
                boolean isEmpty) {
            super.updateItem(entry, isEmpty);

            if (isEmpty || entry == null) {
                setGraphic(null);
                setText(null);
                setStyle("-fx-background-color: transparent;");
            } else {
                setGraphic(new BalanceCard(
                        entry.getKey(),
                        entry.getValue()).getRoot());
                setStyle("-fx-background-color: transparent;"
                        + "-fx-padding: 4 10 4 10;");
            }
        }
    }
}
