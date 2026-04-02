package fairshare.ui;

import java.io.IOException;
import java.util.stream.Collectors;

import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.model.expense.Participant;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * A UI component that displays the details of a single {@code Expense}.
 */
public class ExpenseCard {

    private static final String FXML = "/view/ExpenseCard.fxml";

    private HBox root;

    @FXML
    private Label indexLabel;

    @FXML
    private Label groupLabel;

    @FXML
    private Label expenseNameLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private Label payerLabel;

    @FXML
    private VBox participantsContainer;

    @FXML
    private Label tagsLabel;

    /**
     * Constructs an {@code ExpenseCard} for the given expense and index.
     *
     * @param expense      the expense to display; cannot be null.
     * @param displayIndex the 1-based index of this expense in the list.
     */
    public ExpenseCard(Expense expense, int displayIndex) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    ExpenseCard.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        indexLabel.setText(displayIndex + ". ");
        groupLabel.setText(
                expense.getGroup().getGroupName().toUpperCase());
        expenseNameLabel.setText(expense.getExpenseName());
        amountLabel.setText(
                String.format("$%.2f", expense.getAmount()));

        if (expense.getExpenseType() == ExpenseType.SETTLEMENT) {
            formatSettlement(expense);
        } else {
            formatExpense(expense);
        }

        applyCardStyle(expense);
    }

    /**
     * Returns the root node of this card.
     *
     * @return the root {@code HBox}.
     */
    public HBox getRoot() {
        return root;
    }

    private void applyCardStyle(Expense expense) {
        if (expense.getExpenseType() == ExpenseType.SETTLEMENT) {
            root.setStyle("-fx-background-color: #e8f5e9; "
                    + "-fx-background-radius: 10; "
                    + "-fx-border-radius: 10; "
                    + "-fx-effect: dropshadow(gaussian, "
                    + "rgba(76,175,80,0.15), 8, 0, 0, 2);");
            groupLabel.setStyle(
                    "-fx-text-fill: #ffffff; "
                            + "-fx-font-size: 10; "
                            + "-fx-font-weight: bold; "
                            + "-fx-background-color: #4caf50; "
                            + "-fx-background-radius: 20; "
                            + "-fx-padding: 2 10 2 10;");
        } else {
            root.setStyle("-fx-background-color: #ffffff; "
                    + "-fx-background-radius: 10; "
                    + "-fx-border-radius: 10; "
                    + "-fx-effect: dropshadow(gaussian, "
                    + "rgba(74,127,232,0.12), 8, 0, 0, 2);");
            groupLabel.setStyle(
                    "-fx-text-fill: #ffffff; "
                            + "-fx-font-size: 10; "
                            + "-fx-font-weight: bold; "
                            + "-fx-background-color: #4a7fe8; "
                            + "-fx-background-radius: 20; "
                            + "-fx-padding: 2 10 2 10;");
        }
    }

    private void formatSettlement(Expense expense) {
        payerLabel.setText(expense.getPayer().getName()
                + " → "
                + expense.getParticipants()
                .getFirst().getPerson().getName());
        tagsLabel.setText("");
    }

    private void formatExpense(Expense expense) {
        payerLabel.setText("Paid by: "
                + expense.getPayer().getName());

        int totalShares = expense.getTotalShares();
        double totalAmount = expense.getAmount();

        javafx.scene.layout.FlowPane chipsPane =
                new javafx.scene.layout.FlowPane();
        chipsPane.setHgap(6);
        chipsPane.setVgap(4);

        for (Participant p : expense.getParticipants()) {
            double share = (totalAmount / totalShares)
                    * p.getShares();
            int percentage = (int) Math.round(
                    (p.getShares() * 100.0) / totalShares);

            Label chip = new Label(
                    p.getPerson().getName()
                            + " · " + percentage + "%"
                            + " · $" + String.format("%.2f", share));
            chip.setStyle(
                    "-fx-background-color: #e8eef7;"
                            + "-fx-text-fill: #1a2a4a;"
                            + "-fx-font-size: 11;"
                            + "-fx-background-radius: 20;"
                            + "-fx-border-color: #c5d0e8;"
                            + "-fx-border-radius: 20;"
                            + "-fx-padding: 3 10 3 10;");

            chipsPane.getChildren().add(chip);
        }

        participantsContainer.getChildren().add(chipsPane);

        String tags = expense.getTags().stream()
                .map(t -> t.getTagName())
                .collect(Collectors.joining(", "));
        tagsLabel.setText("Tags: "
                + (tags.isEmpty() ? "-" : tags));
    }
}
