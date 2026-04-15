/**
 * AI declaration --> claude ai was used to help with the colours of the piechart.
 */
package fairshare.ui;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.ui.exceptions.UiException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * A UI component that displays a pie chart showing spending
 * breakdown by tag or group across all expenses.
 */
public class PieChart {

    private static final String FXML = "/view/PieChart.fxml";
    private static final String UNTAGGED_LABEL = "Untagged";

    private static final String[] FIXED_COLOURS = {
        "#0F766E",
        "#0EA5E9",
        "#6366F1",
        "#D97706",
        "#14B8A6",
        "#64748B",
        "#8B5CF6",
        "#84CC16"
    };

    private List<Expense> lastExpenses;

    private VBox root;

    @FXML
    private javafx.scene.chart.PieChart pieChart;

    @FXML
    private Label noDataLabel;

    @FXML
    private ToggleButton byTagButton;

    @FXML
    private ToggleButton byGroupButton;

    /**
     * Constructs a {@code PieChart} with the given list of expenses.
     *
     * @param expenses the list of expenses to display; cannot be null.
     */
    public PieChart(List<Expense> expenses) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    PieChart.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        setupToggle();
        refresh(expenses);
    }

    /**
     * Returns the root node of this component.
     *
     * @return the root {@code VBox}.
     */
    public VBox getRoot() {
        return root;
    }

    /**
     * Refreshes the pie chart with an updated list of expenses.
     *
     * @param expenses the updated list of expenses; cannot be null.
     */
    public void refresh(List<Expense> expenses) {
        lastExpenses = expenses;
        if (byGroupButton.isSelected()) {
            renderByGroup(expenses);
        } else {
            renderByTag(expenses);
        }
    }

    private void setupToggle() {
        ToggleGroup toggleGroup = new ToggleGroup();
        byTagButton.setToggleGroup(toggleGroup);
        byGroupButton.setToggleGroup(toggleGroup);

        byTagButton.setSelected(true);
        updateToggleStyle(byTagButton, byGroupButton);

        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
                return;
            }
            updateToggleStyle(byTagButton, byGroupButton);
            if (lastExpenses != null) {
                refresh(lastExpenses);
            }
        });
    }

    private void updateToggleStyle(ToggleButton tagBtn,
                                   ToggleButton groupBtn) {
        String activeStyle =
                "-fx-font-size: 11;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-color: #0F766E;"
                        + "-fx-text-fill: white;"
                        + "-fx-background-radius: 999;"
                        + "-fx-border-radius: 999;"
                        + "-fx-cursor: hand;"
                        + "-fx-padding: 4 12 4 12;";
        String inactiveStyle =
                "-fx-font-size: 11;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-color: #FFFFFF;"
                        + "-fx-text-fill: #0F766E;"
                        + "-fx-background-radius: 999;"
                        + "-fx-border-color: #CBD5E1;"
                        + "-fx-border-radius: 999;"
                        + "-fx-cursor: hand;"
                        + "-fx-padding: 4 12 4 12;";

        tagBtn.setStyle(tagBtn.isSelected() ? activeStyle : inactiveStyle);
        groupBtn.setStyle(groupBtn.isSelected() ? activeStyle : inactiveStyle);
    }

    private void renderByTag(List<Expense> expenses) {
        Map<String, Double> amounts = new HashMap<>();

        for (Expense expense : expenses) {
            if (expense.getExpenseType() == ExpenseType.SETTLEMENT) {
                continue;
            }
            double amount = expense.getAmount();
            if (expense.getTags().isEmpty()) {
                amounts.merge(UNTAGGED_LABEL, amount, Double::sum);
            } else {
                for (var tag : expense.getTags()) {
                    amounts.merge(tag.getTagName(), amount, Double::sum);
                }
            }
        }

        renderChart(amounts);
    }

    private void renderByGroup(List<Expense> expenses) {
        Map<String, Double> amounts = new HashMap<>();

        for (Expense expense : expenses) {
            if (expense.getExpenseType() == ExpenseType.SETTLEMENT) {
                continue;
            }
            amounts.merge(
                    expense.getGroup().getGroupName(),
                    expense.getAmount(),
                    Double::sum);
        }

        renderChart(amounts);
    }

    private void renderChart(Map<String, Double> amounts) {
        ObservableList<javafx.scene.chart.PieChart.Data> pieData =
                FXCollections.observableArrayList();

        double totalAmount = amounts.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        if (totalAmount <= 0) {
            pieChart.setData(pieData);
            pieChart.setVisible(false);
            pieChart.setManaged(false);
            noDataLabel.setVisible(true);
            noDataLabel.setManaged(true);
            return;
        }

        Map<String, String> colourMap = buildColourMap(amounts);

        amounts.entrySet().stream()
                .sorted(Comparator
                        .comparingDouble((Map.Entry<String, Double> e) -> e.getValue())
                        .reversed()
                        .thenComparing(Map.Entry::getKey, String.CASE_INSENSITIVE_ORDER))
                .forEach(entry -> {
                    String label = entry.getKey();
                    double amount = entry.getValue();
                    int percentage = (int) Math.round((amount / totalAmount) * 100);

                    pieData.add(new javafx.scene.chart.PieChart.Data(
                            label + " $"
                                    + String.format("%.0f", amount)
                                    + " (" + percentage + "%)",
                            amount));
                });

        pieChart.setData(pieData);

        Platform.runLater(() -> {
            for (javafx.scene.chart.PieChart.Data data : pieChart.getData()) {
                String label = data.getName().replaceAll(" \\$.*", "");
                String colour = colourMap.getOrDefault(label, FIXED_COLOURS[0]);
                data.getNode().setStyle("-fx-pie-color: " + colour + ";");
            }
        });

        boolean hasData = !pieData.isEmpty();
        pieChart.setVisible(hasData);
        pieChart.setManaged(hasData);
        noDataLabel.setVisible(!hasData);
        noDataLabel.setManaged(!hasData);
    }

    private Map<String, String> buildColourMap(Map<String, Double> amounts) {
        List<String> labels = amounts.keySet().stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        Map<String, String> colourMap = new HashMap<>();
        for (int i = 0; i < labels.size(); i++) {
            colourMap.put(labels.get(i), FIXED_COLOURS[i % FIXED_COLOURS.length]);
        }
        return colourMap;
    }
}
