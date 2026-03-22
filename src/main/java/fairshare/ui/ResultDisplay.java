package fairshare.ui;

import java.io.IOException;

import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A UI component that displays feedback messages to the user
 * after each command is executed.
 */
public class ResultDisplay {

    private static final String FXML = "/view/ResultDisplay.fxml";

    private Region root;

    @FXML
    private TextArea resultDisplay;

    /**
     * Constructs a {@code ResultDisplay}.
     */
    public ResultDisplay() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    ResultDisplay.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }
    }

    /**
     * Returns the root region of this component.
     *
     * @return the root {@code Region}.
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Sets the feedback text displayed to the user.
     *
     * @param feedbackToUser the message to display; cannot be null.
     */
    public void setFeedbackToUser(String feedbackToUser) {
        resultDisplay.setText(feedbackToUser);
    }
}
