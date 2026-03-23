package fairshare.ui;

import java.io.IOException;

import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A popup window that displays all available commands to the user.
 */
public class HelpWindow {

    private static final String FXML = "/view/HelpWindow.fxml";
    private static final String TITLE = "Help";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private final Stage helpStage;

    /**
     * Constructs a {@code HelpWindow}.
     *
     * @param primaryStage the primary stage of the application;
     *                     cannot be null.
     */
    public HelpWindow(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HelpWindow.class.getResource(FXML));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            helpStage = new Stage();
            helpStage.setTitle(TITLE);
            helpStage.setScene(new Scene(root, WIDTH, HEIGHT));
            helpStage.initModality(Modality.NONE);
            helpStage.initOwner(primaryStage);
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }
    }

    /**
     * Shows the help window.
     */
    public void show() {
        helpStage.show();
        helpStage.requestFocus();
    }

    /**
     * Returns true if the help window is currently showing.
     *
     * @return true if showing, false otherwise.
     */
    public boolean isShowing() {
        return helpStage.isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        helpStage.hide();
    }
}
