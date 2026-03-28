package fairshare.ui;

import java.io.IOException;

import fairshare.logic.Logic;
import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.ui.exceptions.UiException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The main application window. Holds all UI subcomponents and
 * connects the UI to the Logic layer.
 */
public class MainWindow implements Ui {

    private static final String FXML = "/view/MainWindow.fxml";

    private final Stage primaryStage;
    private final Logic logic;

    private ExpenseListPanel expenseListPanel;
    private BalancePanel balancePanel;
    private ResultDisplay resultDisplay;
    private CommandBox commandBox;
    private HelpWindow helpWindow;

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
            Parent root = fxmlLoader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("FairShare");
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }
    }

    /**
     * Injects all subcomponents into their placeholders.
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

        helpWindow = new HelpWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.show();
    }

    /**
     * Displays a startup message in the result display.
     * Used to warn the user about data issues on launch.
     *
     * @param message the message to display; cannot be null.
     */
    public void showStartupMessage(String message) {
        resultDisplay.setFeedbackToUser(message);
    }

    /**
     * Executes the command and refreshes all panels.
     *
     * @param commandText the raw command string entered by the user.
     * @throws CommandException if the command execution fails.
     * @throws ParseException   if the command cannot be parsed.
     */
    private void executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult result = logic.execute(commandText);
            resultDisplay.setFeedbackToUser(result.getResponse());
            if (result.getIsHelp()) {
                helpWindow.show();
                return;
            }
            if (result.getIsExit()) {
                handleExit();
                return;
            }
            balancePanel.refresh(logic.calculateBalances());
        } catch (CommandException | ParseException e) {
            resultDisplay.setFeedbackToUser(e.getMessage());

            throw e; // Rethrow to notify commandbox of an exception
        }
    }

    private void handleExit() {
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> Platform.exit());
        delay.play();
    }
}
