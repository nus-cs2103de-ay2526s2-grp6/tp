package fairshare.ui;

import java.io.IOException;

import fairshare.model.balance.Balance;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A UI component that displays the details of a single {@code Balance}.
 */
public class BalanceCard {

    private static final String FXML = "/view/BalanceCard.fxml";

    private VBox root;

    @FXML
    private Label nameLabel;

    @FXML
    private Label balanceLabel;

    /**
     * Constructs a {@code BalanceCard} for the given balance.
     *
     * @param balance the balance to display; cannot be null.
     */
    public BalanceCard(Balance balance) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    BalanceCard.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        nameLabel.setText(balance.getDebtor().getName());
        String amountText = String.format("owes $%.2f to %s",
                balance.getAmount(),
                balance.getCreditor().getName());
        balanceLabel.setText(amountText);
        balanceLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #cc0000;");
    }

    /**
     * Returns the root node of this card.
     *
     * @return the root {@code HBox}.
     */
    public VBox getRoot() {
        return root;
    }
}
