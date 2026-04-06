package fairshare.ui;

import java.io.IOException;
import java.util.ArrayList;

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
    private PieChart pieChart;
    private StatusBar statusBar;
    private ResultDisplay resultDisplay;
    private CommandBox commandBox;
    private HelpWindow helpWindow;
    private Header header;
    private GroupWindow groupWindow;

    @FXML
    private StackPane expenseListPanelPlaceholder;

    @FXML
    private StackPane balancePanelPlaceholder;

    @FXML
    private StackPane pieChartPlaceholder;

    @FXML
    private StackPane statusBarPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane headerPlaceholder;

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
        header = new Header();
        headerPlaceholder.getChildren().add(header.getRoot());

        expenseListPanel = new ExpenseListPanel(
                logic.getFilteredExpenseList());
        expenseListPanelPlaceholder.getChildren().add(
                expenseListPanel.getRoot());

        balancePanel = new BalancePanel(logic.calculateBalances());
        balancePanelPlaceholder.getChildren().add(
                balancePanel.getRoot());

        pieChart = new PieChart(new ArrayList<>(logic.getFilteredExpenseList()));
        pieChartPlaceholder.getChildren().add(
                pieChart.getRoot());

        statusBar = new StatusBar(logic.getExpenseList());
        statusBarPlaceholder.getChildren().add(
                statusBar.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(
                resultDisplay.getRoot());

        commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(
                commandBox.getRoot());

        helpWindow = new HelpWindow();

        groupWindow = new GroupWindow(primaryStage);
        header.setOnGroupsClicked(() ->
                groupWindow.show(
                        logic.getExpenseList(),
                        logic.calculateBalances()));

        showStartupMessage("Welcome to FairShare! \n" +
                "Add an expense or type 'help' to show all available commands.");

        primaryStage.getScene().getStylesheets().add(
                getClass().getResource("/view/styles.css")
                        .toExternalForm());
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
    private void executeCommand(String commandText)
            throws CommandException, ParseException {
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
            pieChart.refresh(new ArrayList<>(logic.getFilteredExpenseList()));
            statusBar.refresh(logic.getExpenseList());
            groupWindow.refreshIfShowing(
                    logic.getExpenseList(),
                    logic.calculateBalances());

        } catch (CommandException | ParseException e) {
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    private void handleExit() {
        PauseTransition delay = new PauseTransition(
                Duration.seconds(2));
        delay.setOnFinished(event -> Platform.exit());
        delay.play();
    }
}
