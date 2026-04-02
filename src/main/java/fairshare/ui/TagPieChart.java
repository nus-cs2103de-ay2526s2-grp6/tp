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

    private final Map<String, String> tagColourMap = new HashMap<>();
    private int colourIndex = 0;

    private VBox root;

    @FXML
    private PieChart tagPieChart;

    @FXML
    private Label noDataLabel;

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
        Map<String, Double> tagAmounts = new HashMap<>();

        for (Expense expense : expenses) {
            if (expense.getExpenseType() == ExpenseType.SETTLEMENT) {
                continue;
            }

            double amount = expense.getAmount();

            if (expense.getTags().isEmpty()) {
                tagAmounts.merge(UNTAGGED_LABEL, amount,
                        Double::sum);
            } else {
                for (var tag : expense.getTags()) {
                    tagAmounts.merge(tag.getTagName(), amount,
                            Double::sum);
                }
            }
        }

        ObservableList<PieChart.Data> pieData =
                FXCollections.observableArrayList();

        tagAmounts.forEach((tag, amount) -> {
            if (!tagColourMap.containsKey(tag)) {
                tagColourMap.put(tag,
                        FIXED_COLOURS[colourIndex
                                % FIXED_COLOURS.length]);
                colourIndex++;
            }
            pieData.add(new PieChart.Data(
                    tag + String.format(" $%.0f", amount),
                    amount));
        });

        tagPieChart.setData(pieData);

        // apply fixed colours after data is set
        for (PieChart.Data data : tagPieChart.getData()) {
            String label = data.getName()
                    .replaceAll(" \\$.*", "");
            String colour = tagColourMap.getOrDefault(
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
