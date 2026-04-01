package fairshare.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A popup window that displays all groups with their
 * active or settled status.
 */
public class GroupWindow {

    private static final String FXML = "/view/GroupWindow.fxml";
    private static final String TITLE = "Groups";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 500;

    private final Stage groupStage;

    @FXML
    private VBox activeGroupsContainer;

    @FXML
    private VBox pastGroupsContainer;

    @FXML
    private Label activeGroupsHeader;

    @FXML
    private Label pastGroupsHeader;

    @FXML
    private Label noActiveLabel;

    @FXML
    private Label noPastLabel;

    /**
     * Constructs a {@code GroupWindow}.
     *
     * @param primaryStage the primary stage; cannot be null.
     */
    public GroupWindow(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    GroupWindow.class.getResource(FXML));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            groupStage = new Stage();
            groupStage.setTitle(TITLE);
            groupStage.getIcons().add(new Image(
                    getClass().getResourceAsStream("/images/group.png")));
            groupStage.setScene(new Scene(root, WIDTH, HEIGHT));
            groupStage.initModality(Modality.NONE);
            groupStage.initOwner(primaryStage);
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }
    }

    /**
     * Shows the group window with updated data.
     *
     * @param expenses the full list of expenses; cannot be null.
     * @param balances the current list of balances; cannot be null.
     */
    public void show(List<Expense> expenses,
                     List<Balance> balances) {
        refresh(expenses, balances);
        if (!groupStage.isShowing()) {
            groupStage.show();
        }
        groupStage.toFront();
        groupStage.requestFocus();
    }

    private void refresh(List<Expense> expenses,
                         List<Balance> balances) {
        activeGroupsContainer.getChildren().clear();
        pastGroupsContainer.getChildren().clear();

        Map<String, List<Expense>> groupedExpenses =
                new LinkedHashMap<>();
        for (Expense e : expenses) {
            String name = e.getGroup().getGroupName();
            groupedExpenses.computeIfAbsent(
                    name, k -> new ArrayList<>()).add(e);
        }

        List<String> activeDebtor = balances.stream()
                .map(b -> b.getDebtor().getName())
                .toList();

        List<String> activeGroups = new ArrayList<>();
        List<String> pastGroups = new ArrayList<>();

        for (Map.Entry<String, List<Expense>> entry
                : groupedExpenses.entrySet()) {
            String groupName = entry.getKey();
            List<Expense> groupExpenses = entry.getValue();

            boolean hasBalance = groupExpenses.stream()
                    .anyMatch(e -> e.getParticipants()
                            .stream()
                            .anyMatch(p -> activeDebtor
                                    .contains(p.getPerson()
                                            .getName())));

            if (hasBalance) {
                activeGroups.add(groupName);
            } else {
                pastGroups.add(groupName);
            }
        }

        if (activeGroups.isEmpty()) {
            noActiveLabel.setVisible(true);
            noActiveLabel.setManaged(true);
        } else {
            noActiveLabel.setVisible(false);
            noActiveLabel.setManaged(false);
            for (String name : activeGroups) {
                activeGroupsContainer.getChildren().add(
                        createGroupCard(name,
                                groupedExpenses.get(name),
                                true));
            }
        }

        if (pastGroups.isEmpty()) {
            noPastLabel.setVisible(true);
            noPastLabel.setManaged(true);
        } else {
            noPastLabel.setVisible(false);
            noPastLabel.setManaged(false);
            for (String name : pastGroups) {
                pastGroupsContainer.getChildren().add(
                        createGroupCard(name,
                                groupedExpenses.get(name),
                                false));
            }
        }
    }

    private VBox createGroupCard(String groupName,
                                 List<Expense> expenses, boolean isActive) {
        VBox card = new VBox(4);
        card.setStyle(
                "-fx-background-color: #ffffff;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-radius: 8;"
                        + "-fx-effect: dropshadow(gaussian,"
                        + "rgba(74,127,232,0.10), 6, 0, 0, 2);");
        card.setPadding(new Insets(10, 14, 10, 14));

        Label nameLabel = new Label(
                groupName.toUpperCase());
        nameLabel.setStyle(
                "-fx-font-size: 13;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #1a2a4a;");

        double total = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        Label totalLabel = new Label(
                expenses.size() + " expenses  ·  "
                        + String.format("$%.2f total", total));
        totalLabel.setStyle(
                "-fx-font-size: 11;"
                        + "-fx-text-fill: #6b7fa8;");

        Label statusLabel = new Label(
                isActive ? "● Active" : "✓ Settled");
        statusLabel.setStyle(
                "-fx-font-size: 11;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + (isActive ? "#ff9800;" : "#2e7d32;"));

        card.getChildren().addAll(
                nameLabel, totalLabel, statusLabel);
        return card;
    }

    /**
     * Refreshes the group window data if it is currently showing.
     *
     * @param expenses the full list of expenses; cannot be null.
     * @param balances the current list of balances; cannot be null.
     */
    public void refreshIfShowing(List<Expense> expenses,
                                 List<Balance> balances) {
        if (groupStage.isShowing()) {
            refresh(expenses, balances);
        }
    }
}
