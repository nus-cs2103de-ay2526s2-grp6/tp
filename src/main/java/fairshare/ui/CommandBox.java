package fairshare.ui;

import java.io.IOException;

import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * A UI component that accepts text input from the user and executes commands.
 */
public class CommandBox {

    private static final String FXML = "/view/CommandBox.fxml";
    private static final String ERROR_STYLE_CLASS = "error";

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
            commandTextField.setText("");
            commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
        } catch (CommandException | ParseException e) {
            if (!commandTextField.getStyleClass().contains(
                    ERROR_STYLE_CLASS)) {
                commandTextField.getStyleClass().add(ERROR_STYLE_CLASS);
            }
        } catch (Exception e) {
            if (!commandTextField.getStyleClass().contains(
                    ERROR_STYLE_CLASS)) {
                commandTextField.getStyleClass().add(ERROR_STYLE_CLASS);
            }
        }
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
         * @return the {@code CommandResult} from the execution.
         * @throws CommandException if the command execution fails.
         * @throws ParseException   if the command cannot be parsed.
         */
        CommandResult execute(String commandText)
                throws CommandException, ParseException;
    }

    @FXML
    private void initialize() {
        System.out.println("initialize() called");
        commandTextField.setOnAction(event -> handleCommandEntered());
        sendButton.setOnAction(event -> handleCommandEntered());
    }
}
