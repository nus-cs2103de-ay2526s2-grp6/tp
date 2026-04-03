package fairshare.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.ui.exceptions.UiException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * A UI component that displays a pie chart showing spending
 * breakdown by tag or group across all expenses.
 */
public class TagPieChart {

    private static final String FXML = "/view/TagPieChart.fxml";
    private static final String UNTAGGED_LABEL = "Untagged";

    private static final String[] FIXED_COLOURS = {
            "#4a7fe8", "#e8734a", "#4ae87f", "#e8d44a",
            "#a44ae8", "#4ae8d4", "#e84a7f", "#7fe84a"
    };

    private final Map<String, String> colourMap = new HashMap<>();
    private int colourIndex = 0;
    private List<Expense> lastExpenses;

    private VBox root;

    @FXML
    private PieChart tagPieChart;

    @FXML
    private Label noDataLabel;

    @FXML
    private ToggleButton byTagButton;

    @FXML
    private ToggleButton byGroupButton;

    /**
     * Constructs a {@code TagPieChart} with the given list of expenses.
     *
     * @param expenses the list of expenses to display; cannot be null.
     */
    public TagPieChart(List<Expense> expenses) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    TagPieChart.class.getResource(FXML));
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

        toggleGroup.selectedToggleProperty().addListener(
                (obs, oldVal, newVal) -> {
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
                        + "-fx-background-color: #4a7fe8;"
                        + "-fx-text-fill: white;"
                        + "-fx-background-radius: 20;"
                        + "-fx-border-radius: 20;"
                        + "-fx-cursor: hand;"
                        + "-fx-padding: 2 10 2 10;";
        String inactiveStyle =
                "-fx-font-size: 11;"
                        + "-fx-background-color: #e8eef7;"
                        + "-fx-text-fill: #4a7fe8;"
                        + "-fx-background-radius: 20;"
                        + "-fx-border-color: #c5d0e8;"
                        + "-fx-border-radius: 20;"
                        + "-fx-cursor: hand;"
                        + "-fx-padding: 2 10 2 10;";

        tagBtn.setStyle(
                tagBtn.isSelected() ? activeStyle : inactiveStyle);
        groupBtn.setStyle(
                groupBtn.isSelected() ? activeStyle : inactiveStyle);
    }

    private void renderByTag(List<Expense> expenses) {
        Map<String, Double> amounts = new HashMap<>();

        for (Expense expense : expenses) {
            if (expense.getExpenseType()
                    == ExpenseType.SETTLEMENT) {
                continue;
            }
            double amount = expense.getAmount();
            if (expense.getTags().isEmpty()) {
                amounts.merge(UNTAGGED_LABEL, amount,
                        Double::sum);
            } else {
                for (var tag : expense.getTags()) {
                    amounts.merge(tag.getTagName(), amount,
                            Double::sum);
                }
            }
        }

        renderChart(amounts);
    }

    private void renderByGroup(List<Expense> expenses) {
        Map<String, Double> amounts = new HashMap<>();

        for (Expense expense : expenses) {
            if (expense.getExpenseType()
                    == ExpenseType.SETTLEMENT) {
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
        ObservableList<PieChart.Data> pieData =
                FXCollections.observableArrayList();

        amounts.forEach((label, amount) -> {
            if (!colourMap.containsKey(label)) {
                colourMap.put(label,
                        FIXED_COLOURS[colourIndex
                                % FIXED_COLOURS.length]);
                colourIndex++;
            }
            pieData.add(new PieChart.Data(
                    label + String.format(" $%.0f", amount),
                    amount));
        });

        tagPieChart.setData(pieData);

        for (PieChart.Data data : tagPieChart.getData()) {
            String label = data.getName()
                    .replaceAll(" \\$.*", "");
            String colour = colourMap.getOrDefault(
                    label, FIXED_COLOURS[0]);
            data.getNode().setStyle(
                    "-fx-pie-color: " + colour + ";");
        }

        boolean hasData = !pieData.isEmpty();
        tagPieChart.setVisible(hasData);
        tagPieChart.setManaged(hasData);
        noDataLabel.setVisible(!hasData);
        noDataLabel.setManaged(!hasData);
    }
}
