package fairshare.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fairshare.model.balance.Balance;
import fairshare.model.group.Group;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * A UI panel that displays the net balance summary by group,
 * showing who owes whom and how much.
 */
public class BalancePanel {

    private static final String FXML = "/view/BalancePanel.fxml";

    private Region root;

    @FXML
    private Accordion balancesAccordion;

    @FXML
    private Label emptyStateLabel;

    /**
     * Constructs a {@code BalancePanel} with the given map of
     * group balances.
     *
     * @param groupBalances the initial map of group balances
     *                      to display.
     */
    public BalancePanel(Map<Group, List<Balance>> groupBalances) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    BalancePanel.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        setBalances(groupBalances);
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
     * Refreshes the panel with an updated map of group balances.
     *
     * @param groupBalances the updated map of group balances.
     */
    public void refresh(Map<Group, List<Balance>> groupBalances) {
        setBalances(groupBalances);
    }

    private void setBalances(
            Map<Group, List<Balance>> groupBalances) {
        balancesAccordion.getPanes().clear();

        if (groupBalances.isEmpty()) {
            updateEmptyState(true);
            return;
        }

        updateEmptyState(false);

        for (Map.Entry<Group, List<Balance>> entry
                : groupBalances.entrySet()) {
            Group group = entry.getKey();
            List<Balance> debts = entry.getValue();

            TitledPane groupPane = createGroupPane(
                    group, debts);
            balancesAccordion.getPanes().add(groupPane);
        }

        if (!balancesAccordion.getPanes().isEmpty()) {
            balancesAccordion.setExpandedPane(
                    balancesAccordion.getPanes().get(0));
        }
    }

    /**
     * Creates a styled {@code TitledPane} for a group showing
     * its balance cards or a settled message.
     *
     * @param group the group to display.
     * @param debts the list of balances for this group.
     * @return the styled {@code TitledPane}.
     */
    private TitledPane createGroupPane(Group group,
                                       List<Balance> debts) {
        VBox cardContainer = new VBox(8);
        cardContainer.setPadding(new Insets(10, 10, 10, 10));
        cardContainer.setStyle(
                "-fx-background-color: #e8eef7;");

        if (debts.isEmpty()) {
            Label settledLabel = new Label(
                    "✓  All settled up!");
            settledLabel.setStyle(
                    "-fx-text-fill: #2e7d32;"
                            + "-fx-font-size: 12;"
                            + "-fx-font-weight: bold;"
                            + "-fx-background-color: #d0daf0;"
                            + "-fx-background-radius: 6;"
                            + "-fx-padding: 6 12 6 12;");
            cardContainer.getChildren().add(settledLabel);
        } else {
            Map<String, List<Balance>> debtsByPerson =
                    new LinkedHashMap<>();
            for (Balance balance : debts) {
                String debtorName =
                        balance.getDebtor().getName();
                debtsByPerson.computeIfAbsent(
                        debtorName,
                        k -> new ArrayList<>()).add(balance);
            }

            for (Map.Entry<String, List<Balance>> personEntry
                    : debtsByPerson.entrySet()) {
                BalanceCard card = new BalanceCard(
                        personEntry.getKey(),
                        personEntry.getValue());
                cardContainer.getChildren().add(
                        card.getRoot());
            }
        }

        TitledPane groupPane = new TitledPane();
        groupPane.setContent(cardContainer);

        boolean isSettled = debts.isEmpty();
        String statusIndicator = isSettled ? "✓ " : "● ";
        String statusColour = isSettled
                ? "#2e7d32" : "#ff9800";

        Label titleLabel = new Label(
                statusIndicator
                        + group.getGroupName().toUpperCase());
        titleLabel.setStyle(
                "-fx-font-size: 12;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: " + statusColour + ";");

        groupPane.setGraphic(titleLabel);
        groupPane.setText("");
        groupPane.setStyle(
                "-fx-background-color: #d0daf0;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #c5d0e8;"
                        + "-fx-border-radius: 8;"
                        + "-fx-border-width: 1;");

        return groupPane;
    }

    /**
     * Shows or hides the empty state label based on whether
     * the balance list is empty.
     *
     * @param isEmpty whether the balance list is empty.
     */
    private void updateEmptyState(boolean isEmpty) {
        emptyStateLabel.setVisible(isEmpty);
        emptyStateLabel.setManaged(isEmpty);
    }
}
