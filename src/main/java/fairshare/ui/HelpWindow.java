package fairshare.ui;

import java.io.IOException;

import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A popup window that displays all available commands to the user.
 */
public class HelpWindow {

    private static final String FXML = "/view/HelpWindow.fxml";
    private static final String TITLE = "Help";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final String MESSAGE_HELP =
            """
            Add Expense (equal split)
              add n/NAME a/AMOUNT g/GROUP p/PAYER s/PERSON... t/TAG...
              Example: add n/Lunch a/20.0 g/JB p/alice s/alice s/bob t/food

            Add Expense (proportional split)
                add n/NAME a/AMOUNT g/GROUP p/PAYER s/PERSON:PARTS... t/TAG...
                Example: add n/Lunch a/20.0 g/JB p/alice s/alice:3 s/bob:1 t/food
                (alice pays 3/4, bob pays 1/4 of the total)

            Delete Expense
              delete INDEX
              Example: delete 1

            Update Expense
              update INDEX n/NAME a/AMOUNT g/GROUP p/PAYER ... (at least 1)
              Example: update 2 s/john s/mary a/12

            Settle Debt
                settle g/GROUP p/PAYER r/RECEIVER a/AMOUNT
                Example: settle g/JB p/alice r/bob a/10.0
            
            Filter Expenses
              filter n/NAME       (by expense name)
              filter g/GROUP      (by group name)
              filter p/PAYER      (by payer)
              filter s/PERSON     (by participant)
              filter t/TAG        (by tag)
              Example: filter p/alice
              Example: filter g/JB

            List All Expenses
              list

            Clear All Expenses
              clear

            Help
              help

            Exit
              exit""";

    private Image helpIcon = new Image(getClass().getResourceAsStream("/images/help.png"));

    private final Stage helpStage;

    @FXML
    private Label helpMessage;
    /**
     * Constructs a {@code HelpWindow}.
     *
     */
    public HelpWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HelpWindow.class.getResource(FXML));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            helpMessage.setText(MESSAGE_HELP);
            helpStage = new Stage();
            helpStage.getIcons().add(helpIcon);
            helpStage.setTitle(TITLE);
            helpStage.setScene(new Scene(root, WIDTH, HEIGHT));
            helpStage.initModality(Modality.NONE);
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }
    }

    /**
     * Shows the help window.
     */
    public void show() {
        if (!helpStage.isShowing()) {
            helpStage.show();
        }

        if (helpStage.isIconified()) { // Check if minimized
            helpStage.setIconified(false);
        }

        helpStage.toFront();
        helpStage.requestFocus();
    }
}
