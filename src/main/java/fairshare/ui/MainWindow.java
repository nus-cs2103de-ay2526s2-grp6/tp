package fairshare.ui;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import fairshare.logic.Logic;
import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Expense;
import fairshare.ui.exceptions.UiException;

/**
 * The main application window. Holds all UI sub-components and
 * connects the UI to the Logic layer.
 */
public class MainWindow {

    private static final String FXML = "/view/MainWindow.fxml";

    private final Stage primaryStage;
    private final Logic logic;

    private ExpenseListPanel expenseListPanel;
    private BalancePanel balancePanel;
    private ResultDisplay resultDisplay;
    private CommandBox commandBox;

    @FXML
    private StackPane expenseListPanelPlaceholder;

    @FXML
    private StackPane balancePanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;

    /**
     * Constructs a {@code MainWindow} with the given stage and logic.
     *
     * @param primaryStage the main JavaFX stage; cannot be null.
     * @param logic        the logic component; cannot be null.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        this.primaryStage = primaryStage;
        this.logic = logic;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    MainWindow.class.getResource(FXML));
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }
    }

    /**
     * Returns the primary stage of this window.
     *
     * @return the {@code Stage}.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Injects all sub-components into their placeholders.
     */
    public void fillInnerParts() {
        expenseListPanel = new ExpenseListPanel(
                logic.getFilteredExpenseList());
        expenseListPanelPlaceholder.getChildren().add(
                expenseListPanel.getRoot());

        balancePanel = new BalancePanel(logic.calculateBalances());
        balancePanelPlaceholder.getChildren().add(
                balancePanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(
                resultDisplay.getRoot());

        commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(
                commandBox.getRoot());
    }

    /**
     * Shows the primary stage.
     */
    public void show() {
        primaryStage.show();
    }

    /**
     * Executes the command and refreshes all panels.
     *
     * @param commandText the raw command string entered by the user.
     * @return the {@code CommandResult} from executing the command.
     * @throws CommandException if the command execution fails.
     * @throws ParseException   if the command cannot be parsed.
     */
    private CommandResult executeCommand(String commandText)
            throws CommandException, ParseException {
        CommandResult result = logic.execute(commandText);

        resultDisplay.setFeedbackToUser(result.getResponse());
        expenseListPanel.refresh(logic.getFilteredExpenseList());
        balancePanel.refresh(logic.calculateBalances());

        return result;
    }
}
