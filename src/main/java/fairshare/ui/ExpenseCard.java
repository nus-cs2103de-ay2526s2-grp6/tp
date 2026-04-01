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
import javafx.scene.layout.HBox;

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
    private Label participantsLabel;

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
        groupLabel.setText(expense.getGroup().getGroupName().toUpperCase());
        expenseNameLabel.setText(expense.getExpenseName());
        amountLabel.setText(String.format("$%.2f", expense.getAmount()));

        if (expense.getExpenseType() == ExpenseType.SETTLEMENT) {
            formatSettlement(expense);
        } else {
            formatExpense(expense);
        }

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
        } else if (expense.getAmount() >= 100) {
            root.setStyle("-fx-background-color: #fff8e1; "
                    + "-fx-background-radius: 10; "
                    + "-fx-border-radius: 10; "
                    + "-fx-effect: dropshadow(gaussian, "
                    + "rgba(255,152,0,0.15), 8, 0, 0, 2);");
            groupLabel.setStyle(
                    "-fx-text-fill: #ffffff; "
                            + "-fx-font-size: 10; "
                            + "-fx-font-weight: bold; "
                            + "-fx-background-color: #ff9800; "
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

    /**
     * Returns the root node of this card.
     *
     * @return the root {@code HBox}.
     */
    public HBox getRoot() {
        return root;
    }

    /**
     * Formats a participant's name with their percentage and
     * dollar amount.
     * For example, bob with 2 shares out of 3 total on a $40
     * expense displays as "bob (67% · $26.67)".
     *
     * @param participant the participant to format; cannot be null.
     * @param totalShares the total number of shares in the expense.
     * @param totalAmount the total amount of the expense.
     * @return a formatted string showing name, percentage and amount.
     */
    private String formatParticipant(Participant participant,
                                     int totalShares, double totalAmount) {
        int percentage = (int) Math.round(
                (participant.getShares() * 100.0) / totalShares);
        double share = (totalAmount / totalShares)
                * participant.getShares();
        return participant.getPerson().getName()
                + " (" + percentage + "% · $"
                + String.format("%.2f", share) + ")";
    }

    private void formatSettlement(Expense expense) {
        payerLabel.setText(expense.getPayer().getName() + " -> "
                + expense.getParticipants().getFirst().getPerson().getName());

    }

    private void formatExpense(Expense expense) {
        payerLabel.setText("Paid by: "
                + expense.getPayer().getName());

        int totalShares = expense.getTotalShares();
        double totalAmount = expense.getAmount();

        String participants = expense.getParticipants().stream()
                .map(p -> formatParticipant(
                        p, totalShares, totalAmount))
                .collect(Collectors.joining(", "));
        participantsLabel.setText("Participants: " + participants);

        String tags = expense.getTags().stream()
                .map(t -> t.getTagName())
                .collect(Collectors.joining(", "));
        tagsLabel.setText("Tags: "
                + (tags.isEmpty() ? "-" : tags));
    }
}
