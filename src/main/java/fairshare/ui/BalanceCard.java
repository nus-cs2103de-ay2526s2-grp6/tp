package fairshare.ui;

import java.io.IOException;
import java.util.List;

import fairshare.model.balance.Balance;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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
            Label debtLabel = new Label(
                    String.format("owes $%.2f to %s",
                            balance.getAmount(),
                            balance.getCreditor().getName()));
            debtLabel.setStyle(
                    "-fx-font-size: 12; -fx-text-fill: #c62828;");
            debtsContainer.getChildren().add(debtLabel);
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
