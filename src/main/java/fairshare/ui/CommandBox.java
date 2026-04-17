/**
 * AI declaration --> claude ai was used to help complete this class,
 * namely how to load the different sections
 *
 */

package fairshare.ui;

import java.io.IOException;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;

/**
 * A UI component that accepts text input from the user and executes commands.
 */
public class CommandBox {

    private static final String FXML = "/view/CommandBox.fxml";
    private static final String ERROR_STYLE =
            "-fx-background-color: #FEF2F2;"
                    + "-fx-border-color: #FCA5A5;"
                    + "-fx-text-fill: #B91C1C;"
                    + "-fx-prompt-text-fill: #B91C1C;";

    private final CommandExecutor commandExecutor;
    private Region root;

    @FXML
    private TextField commandTextField;

    @FXML
    private Button sendButton;

    /**
     * Constructs a {@code CommandBox} with the given command executor.
     *
     * @param commandExecutor the executor that handles command strings;
     *                        cannot be null.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    CommandBox.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        commandTextField.textProperty().addListener((
                observable, oldValue, newValue) -> clearErrorStyle());
    }

    /**
     * Returns the root region of this component.
     *
     * @return the root {@code Region}.
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Handles the Enter key press. Executes the command and clears
     * the text field on success, or shows an error style on failure.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.isBlank()) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.clear();
            clearErrorStyle();
        } catch (CommandException | ParseException e) {
            applyErrorStyle();
        }
    }

    private void clearErrorStyle() {
        commandTextField.setStyle("");
    }

    private void applyErrorStyle() {
        commandTextField.setStyle(ERROR_STYLE);
    }

    /**
     * Represents a function that executes a command string.
     */
    @FunctionalInterface
    public interface CommandExecutor {

        /**
         * Executes the given command text.
         *
         * @param commandText the raw command string.
         * @throws CommandException if the command execution fails.
         * @throws ParseException   if the command cannot be parsed.
         */
        void execute(String commandText) throws CommandException, ParseException;
    }

    @FXML
    private void initialize() {
        commandTextField.setOnAction(event -> handleCommandEntered());
        sendButton.setOnAction(event -> handleCommandEntered());

        commandTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                commandTextField.clear();
                clearErrorStyle();
            }
        });
    }
}
