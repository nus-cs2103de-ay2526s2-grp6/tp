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
 * The main application class. Initialises all components and starts the UI.
 */
public class FairShare extends Application {

    private static final Path DATA_FILE_PATH =
            Paths.get("data", "expenses.txt");

    private Logic logic;
    private Storage storage;

    /**
     * Initialises Storage, Model and Logic, and loads saved expenses.
     *
     * @throws Exception if initialisation fails.
     */
    @Override
    public void init() throws Exception {
        storage = new StorageManager(new TxtFairShareStorage(DATA_FILE_PATH));

        Model model;
        try {
            List<Expense> savedExpenses = storage.readFairShare();
            model = new ModelManager(savedExpenses);
        } catch (StorageException e) {
            model = new ModelManager();
        }

        logic = new LogicManager(model, storage);
    }

    /**
     * Creates and shows the main window.
     *
     * @param primaryStage the primary stage; cannot be null.
     */
    @Override
    public void start(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow(primaryStage, logic);
        mainWindow.fillInnerParts();
        mainWindow.start(primaryStage);
    }
}
