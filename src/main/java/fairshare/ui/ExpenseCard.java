package fairshare.ui;

import java.io.IOException;
import java.util.stream.Collectors;

import fairshare.model.expense.Expense;
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

        int totalShares = expense.getTotalShares();

        indexLabel.setText(displayIndex + ". ");
        expenseNameLabel.setText(expense.getExpenseName());
        amountLabel.setText(String.format("$%.2f", expense.getAmount()));
        payerLabel.setText("Paid by: " + expense.getPayer().getName());

        String participants = expense.getParticipants().stream()
                .map(p -> formatParticipant(p, totalShares))
                .collect(Collectors.joining(", "));
        participantsLabel.setText("Participants: " + participants);

        String tags = expense.getTags().stream()
                .map(t -> t.getTagName())
                .collect(Collectors.joining(", "));
        tagsLabel.setText("Tags: " + (tags.isEmpty() ? "-" : tags));
    }

    /**
     * Format a participant's name with their proportion as a fraction.
     * For example, bob with 2 shares out of 3 total displays as "bob (2/3)".
     *
     * @param participant the participant to format; cannot be null.
     * @param totalShares the total number of shares in the expense.
     * @return a formatted string showing the participant's name and fraction.
     */
    private String formatParticipant(Participant participant, int totalShares) {
        return participant.getPerson().getName()
                + "("
                + participant.getShares()
                + "/"
                + totalShares
                + ")";
    }

    /**
     * Returns the root node of this card.
     *
     * @return the root {@code HBox}.
     */
    public HBox getRoot() {
        return root;
    }
}
