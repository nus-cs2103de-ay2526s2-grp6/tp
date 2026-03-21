package fairshare.ui;

import javafx.stage.Stage;

/**
 * Represents the entry point for the application's user interface.
 * Implementations are responsible for initialising and displaying
 * the primary application window
 */
public interface Ui {

    /**
     * Starts the UI by setting up and showing the primary stage.
     *
     * @param primaryStage the main JavaFX stage provided by tge application;
     *                     cannot be null.
     */
    void start(Stage primaryStage);
}
