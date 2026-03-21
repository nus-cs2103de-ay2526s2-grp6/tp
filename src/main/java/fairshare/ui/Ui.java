package fairshare.ui;

import javafx.stage.Stage;

/**
 * Represents the entry point for the application's user interface.
 */
public interface Ui {

    /**
     * Starts the UI and shows the primary stage.
     *
     * @param primaryStage the main JavaFX stage; cannot be null.
     */
    void start(Stage primaryStage);
}
