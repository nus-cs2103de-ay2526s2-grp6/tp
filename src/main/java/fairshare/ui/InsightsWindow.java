package fairshare.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.model.group.Group;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A popup window that displays group statistics for all groups.
 */
public class InsightsWindow {

    private static final String FXML = "/view/InsightsWindow.fxml";
    private static final String TITLE = "Insights";
    private static final int WIDTH = 420;
    private static final int HEIGHT = 550;

    private static final String ACTIVE_COLOUR = "#D97706";
    private static final String ACTIVE_SOFT = "#FFF7ED";
    private static final String SETTLED_COLOUR = "#15803D";
    private static final String SETTLED_SOFT = "#F0FDF4";
    private static final String TEXT_DARK = "#0F172A";
    private static final String TEXT_MUTED = "#64748B";
    private static final String CARD_BG = "#FFFFFF";
    private static final String CARD_STYLE =
            "-fx-background-color: " + CARD_BG + ";"
                    + "-fx-background-radius: 14;"
                    + "-fx-border-color: #E2E8F0;"
                    + "-fx-border-radius: 14;"
                    + "-fx-border-width: 1;"
                    + "-fx-effect: dropshadow(gaussian,"
                    + "rgba(15,23,42,0.06), 18, 0, 0, 4);";

    private final Stage insightStage;

    @FXML
    private VBox activeGroupsContainer;

    @FXML
    private VBox pastGroupsContainer;

    @FXML
    private Label noActiveLabel;

    @FXML
    private Label noPastLabel;

    /**
     * Constructs a {@code InsightsWindow}.
     *
     * @param primaryStage the primary stage; cannot be null.
     */
    public InsightsWindow(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    InsightsWindow.class.getResource(FXML));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            insightStage = new Stage();
            insightStage.setTitle(TITLE);
            insightStage.getIcons().add(new Image(
                    getClass().getResourceAsStream("/images/insights.png")));
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            scene.getStylesheets().add(
                    getClass().getResource("/view/styles.css")
                            .toExternalForm());
            insightStage.setScene(scene);
            insightStage.initModality(Modality.NONE);
            insightStage.initOwner(primaryStage);
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }
    }

    /**
     * Shows the group window with updated data.
     *
     * @param expenses      the full list of expenses; cannot be null.
     * @param groupBalances the current map of group balances;
     *                      cannot be null.
     */
    public void show(List<Expense> expenses,
                     Map<Group, List<Balance>> groupBalances) {
        refresh(expenses, groupBalances);
        if (!insightStage.isShowing()) {
            insightStage.show();
        }
        insightStage.toFront();
        insightStage.requestFocus();
    }

    /**
     * Refreshes the insights window data if it is currently showing.
     *
     * @param expenses      the full list of expenses; cannot be null.
     * @param groupBalances the current map of group balances;
     *                      cannot be null.
     */
    public void refreshIfShowing(List<Expense> expenses,
                                 Map<Group, List<Balance>> groupBalances) {
        if (insightStage.isShowing()) {
            refresh(expenses, groupBalances);
        }
    }

    private void refresh(List<Expense> expenses,
                         Map<Group, List<Balance>> balances) {
        activeGroupsContainer.getChildren().clear();
        pastGroupsContainer.getChildren().clear();

        Map<String, List<Expense>> groupedExpenses =
                new LinkedHashMap<>();
        for (Expense e : expenses) {
            String name = e.getGroup().getGroupName();
            groupedExpenses.computeIfAbsent(
                    name, k -> new ArrayList<>()).add(e);
        }

        // build a map of group name -> has outstanding balance
        // by looking directly at the balances map
        Map<String, Boolean> groupHasBalance = new LinkedHashMap<>();
        for (Map.Entry<Group, List<Balance>> entry
                : balances.entrySet()) {
            groupHasBalance.put(
                    entry.getKey().getGroupName(),
                    !entry.getValue().isEmpty());
        }

        List<String> activeGroups = new ArrayList<>();
        List<String> pastGroups = new ArrayList<>();

        for (String groupName : groupedExpenses.keySet()) {
            boolean hasBalance = groupHasBalance.getOrDefault(
                    groupName, false);
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

        List<Expense> actualExpenses = expenses.stream()
                .filter(e -> e.getExpenseType()
                        == ExpenseType.EXPENSE)
                .toList();

        long numOfExpenses = actualExpenses.size();
        long numOfMembers = actualExpenses.stream()
                .flatMap(e -> e.getParticipants().stream())
                .map(p -> p.getPerson().getName())
                .distinct()
                .count();
        double totalAmount = actualExpenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        Optional<Expense> biggestExpense = actualExpenses.stream()
                .max(Comparator.comparingDouble(Expense::getAmount));

        Optional<String> topSpender = actualExpenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getPayer().getName(),
                        Collectors.summingDouble(Expense::getAmount)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        Optional<String> topTag = actualExpenses.stream()
                .flatMap(e -> e.getTags().stream())
                .collect(Collectors.groupingBy(
                        t -> t.getTagName(),
                        Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        VBox card = new VBox(10);
        card.setStyle(CARD_STYLE);
        card.setPadding(new Insets(14, 16, 14, 16));

        HBox headerRow = new HBox(8);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(groupName);
        nameLabel.setStyle(
                "-fx-font-size: 15;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: " + TEXT_DARK + ";");

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        String statusColour = isActive
                ? ACTIVE_COLOUR : SETTLED_COLOUR;
        String statusSoftColour = isActive
                ? ACTIVE_SOFT : SETTLED_SOFT;
        Label statusBadge = new Label(
                isActive ? "Active" : "Settled");
        statusBadge.setStyle(
                "-fx-font-size: 10;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: " + statusColour + ";"
                        + "-fx-background-color: " + statusSoftColour + ";"
                        + "-fx-background-radius: 999;"
                        + "-fx-padding: 4 10 4 10;");

        headerRow.getChildren().addAll(
                nameLabel, headerSpacer, statusBadge);

        Label summaryLabel = new Label(
                numOfExpenses + " expenses  ·  "
                        + numOfMembers + " members  ·  "
                        + String.format("$%.2f total", totalAmount));
        summaryLabel.setStyle(
                "-fx-font-size: 11;"
                        + "-fx-text-fill: " + TEXT_MUTED + ";");

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #E2E8F0;");

        VBox statsBox = new VBox(6);

        if (actualExpenses.isEmpty()) {
            Label noDataLabel = new Label(
                    "No expenses recorded yet.");
            noDataLabel.setStyle(
                    "-fx-font-size: 12;"
                            + "-fx-text-fill: " + TEXT_MUTED + ";");
            statsBox.getChildren().add(noDataLabel);
        } else {
            biggestExpense.ifPresent(e ->
                    statsBox.getChildren().add(createStatRow(
                            "Biggest expense",
                            e.getExpenseName()
                                    + "  ($"
                                    + String.format("%.2f", e.getAmount())
                                    + ")")));

            topSpender.ifPresent(spender -> {
                double topAmount = actualExpenses.stream()
                        .filter(e -> e.getPayer().getName()
                                .equals(spender))
                        .mapToDouble(Expense::getAmount)
                        .sum();
                statsBox.getChildren().add(createStatRow(
                        "Top spender",
                        spender
                                + "  ($"
                                + String.format("%.2f", topAmount)
                                + " paid)"));
            });

            topTag.ifPresent(tag ->
                    statsBox.getChildren().add(
                            createStatRow("Top tag", tag)));
        }

        card.getChildren().addAll(
                headerRow, summaryLabel, separator, statsBox);

        return card;
    }

    /**
     * Creates a single stat row with a label and value.
     *
     * @param label the stat label e.g. "Top spender".
     * @param value the stat value e.g. "alice ($40 paid)".
     * @return an {@code HBox} containing the stat row.
     */
    private HBox createStatRow(String label, String value) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelNode = new Label(label);
        labelNode.setStyle(
                "-fx-font-size: 11;"
                        + "-fx-text-fill: " + TEXT_MUTED + ";"
                        + "-fx-min-width: 120;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valueNode = new Label(value);
        valueNode.setStyle(
                "-fx-font-size: 11;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: " + TEXT_DARK + ";");

        row.getChildren().addAll(labelNode, spacer, valueNode);
        return row;
    }
}
