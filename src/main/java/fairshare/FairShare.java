package fairshare;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import fairshare.logic.Logic;
import fairshare.logic.LogicManager;
import fairshare.model.Model;
import fairshare.model.ModelManager;
import fairshare.model.expense.Expense;
import fairshare.storage.Storage;
import fairshare.storage.StorageManager;
import fairshare.storage.TxtFairShareStorage;
import fairshare.storage.exceptions.StorageException;
import fairshare.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main application class. Initialises all components and starts
 * the UI.
 */
public class FairShare extends Application {

    private static final Path DATA_FILE_PATH =
            Paths.get("data", "expenses.txt");

    private Logic logic;
    private Storage storage;
    private String startupMessage = "";

    /**
     * Entry point called by {@code Launcher}.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initialises Storage, Model and Logic, and loads saved expenses.
     * If the data file is corrupted, clears it and stores a warning
     * message to display on startup.
     *
     * @throws Exception if initialisation fails.
     */
    @Override
    public void init() throws Exception {
        storage = new StorageManager(
                new TxtFairShareStorage(DATA_FILE_PATH));
        Model model = new ModelManager();

        try {
            List<Expense> savedExpenses = storage.readFairShare();
            savedExpenses.forEach(model::addExpense);
        } catch (StorageException e) {
            startupMessage = "⚠ WARNING: " + e.getMessage();
        }

        logic = new LogicManager(model, storage);
    }

    /**
     * Creates and shows the main window. Displays a startup warning
     * if the data file was corrupted on load.
     *
     * @param primaryStage the primary stage; cannot be null.
     */
    @Override
    public void start(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow(primaryStage, logic);
        mainWindow.fillInnerParts();
        mainWindow.start(primaryStage);

        if (!startupMessage.isEmpty()) {
            mainWindow.showStartupMessage(startupMessage);
        }
    }

    /**
     * Saves all expenses to disk when the application closes.
     *
     * @throws Exception if saving fails.
     */
    @Override
    public void stop() throws Exception {
        try {
            storage.saveFairShare(
                    logic.getFilteredExpenseList());
        } catch (StorageException e) {
            System.out.println(
                    "Could not save data: " + e.getMessage());
        }
    }
}
