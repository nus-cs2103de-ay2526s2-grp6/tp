/**AI declaration --> claude ai was used to help complete this class,
 * namely how to load the different sections,
 * and how to bring everything together in the main window
 */

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
    private InsightsWindow insightsWindow;

    //Each placeholder is an empty StackPane defined in MainWindow.fxml
    //Sub-panel root nodes are placed into these placeholders in fillInnerParts()
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
     * @param primaryStage the main JavaFX stage
     * @param logic        the logic component
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        assert primaryStage != null : "primaryStage should not be null";
        assert logic != null : "logic should not be null";

        this.primaryStage = primaryStage;
        this.logic = logic;

        //Load the FXML layout and set this class as the controller
        //use setController(this) instead of fx:controller in FXML to avoid
        //the "Controller value already specified" error
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
        //header must be first
        header = new Header();
        headerPlaceholder.getChildren().add(header.getRoot());

        //expense list panel --> bound to the filtered expense list
        //so it automatically updates when the filter changes
        expenseListPanel = new ExpenseListPanel(
                logic.getFilteredExpenseList());
        expenseListPanelPlaceholder.getChildren().add(
                expenseListPanel.getRoot());

        //balance panel --> shows debts grouped by group name in an accordion
        //uses calculateBalances() which is the filtered list
        balancePanel = new BalancePanel(logic.calculateBalances());
        balancePanelPlaceholder.getChildren().add(
                balancePanel.getRoot());

        //pie chart --> initially shows the full filtered expense list
        pieChart = new PieChart(new ArrayList<>(logic.getFilteredExpenseList()));
        pieChartPlaceholder.getChildren().add(
                pieChart.getRoot());

        //status bar --> always shows totals for the full unfiltered list
        //so that the count and total are not affected by filters
        statusBar = new StatusBar(logic.getExpenseList());
        statusBarPlaceholder.getChildren().add(
                statusBar.getRoot());

        //result display --> shows feedback messages after each command
        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(
                resultDisplay.getRoot());

        //command box --> every command typed by the user flows through executeCommand()
        commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(
                commandBox.getRoot());

        //help window --> not added to a placeholder since it is a separate popup window
        helpWindow = new HelpWindow();

        // Insights window --> separate popup Stage showing group stats.
        // Uses calculateAllBalances() (not calculateBalances()) so active/
        // settled status is always based on the full unfiltered list,
        // regardless of what filter is currently active
        insightsWindow = new InsightsWindow(primaryStage);
        header.setOnInsightsClicked(() ->
                insightsWindow.show(
                        logic.getExpenseList(),
                        logic.calculateAllBalances()));

        //show startup message in the result display on first launch
        showStartupMessage("Welcome to FairShare! \n"
                + "Add an expense or type 'help' to show all available commands.");

        primaryStage.getScene().getStylesheets().add(
                getClass().getResource("/view/styles.css")
                        .toExternalForm());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) {
        assert primaryStage != null : "primaryStage should not be null";

        primaryStage.show();
    }

    /**
     * Displays a startup message in the result display.
     *
     * @param message the message to display
     */
    public void showStartupMessage(String message) {
        assert message != null : "message should not be null";

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
            statusBar.refresh(logic.getFilteredExpenseList());
            insightsWindow.refreshIfShowing(
                    logic.getExpenseList(),
                    logic.calculateAllBalances());

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
