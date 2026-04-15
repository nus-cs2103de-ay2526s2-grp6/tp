package fairshare.ui;

import java.io.IOException;
import java.util.List;

import fairshare.model.balance.Balance;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A UI component that displays all debts of a single person.
 */
public class BalanceCard {

    private static final String FXML = "/view/BalanceCard.fxml";

    private VBox root;

    @FXML
    private Label nameLabel;

    @FXML
    private VBox debtsContainer;

    /**
     * Constructs a {@code BalanceCard} for the given person's balances.
     *
     * @param personName the name of the debtor; cannot be null.
     * @param balances   the list of balances for this person;
     *                   cannot be null or empty.
     */
    // AI declaration --> claude ai was used here to help complete the implementation of this method
    public BalanceCard(String personName, List<Balance> balances) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    BalanceCard.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        nameLabel.setText(personName);

        for (Balance balance : balances) {
            Text owesText = new Text("owes ");
            owesText.setStyle(
                    "-fx-fill: #64748B;"
                            + "-fx-font-size: 12;");

            Text amountText = new Text(
                    String.format("$%.2f", balance.getAmount()));
            amountText.setStyle(
                    "-fx-fill: #DC2626;"
                            + "-fx-font-size: 12;"
                            + "-fx-font-weight: bold;");

            Text toText = new Text(
                    " to " + balance.getCreditor().getName());
            toText.setStyle(
                    "-fx-fill: #475569;"
                            + "-fx-font-size: 12;");

            TextFlow debtFlow = new TextFlow(
                    owesText, amountText, toText);
            debtFlow.setLineSpacing(2);

            debtsContainer.getChildren().add(debtFlow);
        }
    }

    /**
     * Returns the root node of this card.
     *
     * @return the root {@code VBox}.
     */
    public VBox getRoot() {
        return root;
    }
}
