package fairshare.ui;

import java.io.IOException;

import fairshare.logic.Logic;
import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The main application window. Holds all UI sub-components and
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
     * Returns the primary stage of this window.
     *
     * @return the {@code Stage}.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
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

        helpWindow = new HelpWindow(primaryStage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) {
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
        try {
            CommandResult result = logic.execute(commandText);

            resultDisplay.setFeedbackToUser(result.getResponse());
            expenseListPanel.refresh(logic.getFilteredExpenseList());
            balancePanel.refresh(logic.calculateBalances());

            return result;
        } catch (CommandException | ParseException e) {
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        } catch (Exception e) {
            resultDisplay.setFeedbackToUser(
                    "Error: " + e.getMessage());
            throw new CommandException(e.getMessage());
        }
    }
}
