/**
 * AI declaration --> claude ai was used to help complete this class,
 * namely how to load the different sections
 *
 */

package fairshare.ui;

import java.io.IOException;

import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * A UI component that displays the application header.
 */
public class Header {

    private static final String FXML = "/view/Header.fxml";

    private HBox root;

    @FXML
    private Button insightsButton;

    /**
     * Constructs a {@code Header}.
     */
    public Header() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    Header.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }
    }

    /**
     * Returns the root node of this component.
     *
     * @return the root {@code HBox}.
     */
    public HBox getRoot() {
        return root;
    }

    /**
     * Sets the handler called when the Insights button is clicked.
     *
     * @param handler the handler to call; cannot be null.
     */
    public void setOnInsightsClicked(Runnable handler) {
        insightsButton.setOnAction(e -> handler.run());
    }
}
