/**AI declaration --> claude ai was used to help complete this class,
 * namely how to load the different sections
 */

package fairshare.ui;

import java.io.IOException;
import java.util.stream.Collectors;

import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.model.expense.Participant;
import fairshare.model.person.Person;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
     * @param expense      the expense to display
     * @param displayIndex the 1-based index (expense id) of this expense in the list.
     */
    public ExpenseCard(Expense expense, int displayIndex) {
        assert expense != null : "expense should not be null";
        assert displayIndex > 0 : "displayIndex should be greater than 0";

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
                expense.getGroup().getGroupName());
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
            root.setStyle("-fx-background-color: #F0FDF4; "
                    + "-fx-background-radius: 14; "
                    + "-fx-border-color: #BBF7D0; "
                    + "-fx-border-radius: 14; "
                    + "-fx-border-width: 1; "
                    + "-fx-effect: dropshadow(gaussian, "
                    + "rgba(22,163,74,0.10), 16, 0, 0, 4);");
            groupLabel.setStyle(
                    "-fx-text-fill: #166534; "
                            + "-fx-font-size: 10; "
                            + "-fx-font-weight: bold; "
                            + "-fx-background-color: #DCFCE7; "
                            + "-fx-background-radius: 999; "
                            + "-fx-padding: 4 10 4 10;");
            amountLabel.setStyle(
                    "-fx-text-fill: #15803D; "
                            + "-fx-font-size: 14; "
                            + "-fx-font-weight: bold;");
            payerLabel.setStyle(
                    "-fx-text-fill: #166534; "
                            + "-fx-font-size: 12; "
                            + "-fx-font-weight: bold;");
        } else {
            root.setStyle("-fx-background-color: #FFFFFF; "
                    + "-fx-background-radius: 14; "
                    + "-fx-border-color: #E2E8F0; "
                    + "-fx-border-radius: 14; "
                    + "-fx-border-width: 1; "
                    + "-fx-effect: dropshadow(gaussian, "
                    + "rgba(15,23,42,0.06), 18, 0, 0, 4);");
            groupLabel.setStyle(
                    "-fx-text-fill: #0369A1; "
                            + "-fx-font-size: 10; "
                            + "-fx-font-weight: bold; "
                            + "-fx-background-color: #E0F2FE; "
                            + "-fx-background-radius: 999; "
                            + "-fx-padding: 4 10 4 10;");
            amountLabel.setStyle(
                    "-fx-text-fill: #15803D; "
                            + "-fx-font-size: 14; "
                            + "-fx-font-weight: bold;");
            payerLabel.setStyle(
                    "-fx-text-fill: #475569; "
                            + "-fx-font-size: 12; "
                            + "-fx-font-weight: bold;");
        }
    }

    private void formatSettlement(Expense expense) {
        Person payer = expense.getParticipants().iterator().next().getPerson();

        payerLabel.setText(expense.getPayer().getName()
                + " → "
                + payer.getName());
        tagsLabel.setText("");
    }

    private void formatExpense(Expense expense) {
        payerLabel.setText("Paid by "
                + expense.getPayer().getName());

        int totalShares = expense.getTotalShares();
        double totalAmount = expense.getAmount();

        javafx.scene.layout.FlowPane chipsPane =
                new javafx.scene.layout.FlowPane();
        chipsPane.setHgap(6);
        chipsPane.setVgap(6);

        for (Participant p : expense.getParticipants()) {
            double share = (totalAmount / totalShares)
                    * p.getShares();
            int percentage = (int) Math.round((p.getShares() * 100.0) / totalShares);

            Label chip = new Label(
                    p.getPerson().getName()
                            + " · " + percentage + "%"
                            + " · $" + String.format("%.2f", share));
            chip.setStyle(
                    "-fx-background-color: #F8FAFC;"
                            + "-fx-text-fill: #334155;"
                            + "-fx-font-size: 11;"
                            + "-fx-background-radius: 999;"
                            + "-fx-border-color: #E2E8F0;"
                            + "-fx-border-radius: 999;"
                            + "-fx-padding: 4 12 4 12;");

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
