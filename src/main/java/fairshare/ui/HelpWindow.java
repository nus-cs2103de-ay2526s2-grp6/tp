package fairshare.ui;

import java.io.IOException;

import fairshare.ui.exceptions.UiException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A popup window that displays all available commands to the user.
 */
public class HelpWindow {

    private static final String FXML = "/view/HelpWindow.fxml";
    private static final String TITLE = "Help";
    private static final int WIDTH = 700;
    private static final int HEIGHT = 640;

    private static final String TEXT_DARK = "#0F172A";
    private static final String TEXT_MUTED = "#64748B";
    private static final String HEADING = "#1A2A4A";
    private static final String BORDER = "#E2E8F0";

    private static final String SLATE = "#475569";
    private static final String SLATE_SOFT = "#F8FAFC";
    private static final String BLUE = "#2563EB";
    private static final String BLUE_SOFT = "#DBEAFE";
    private static final String INFO = "#1D4ED8";
    private static final String INFO_SOFT = "#EFF6FF";

    private static final String CARD_STYLE =
            "-fx-background-color: #FFFFFF;"
                    + "-fx-background-radius: 14;"
                    + "-fx-border-color: " + BORDER + ";"
                    + "-fx-border-radius: 14;"
                    + "-fx-border-width: 1;"
                    + "-fx-effect: dropshadow(gaussian,"
                    + "rgba(15,23,42,0.06), 18, 0, 0, 4);";

    private static final String CODE_BOX_STYLE =
            "-fx-background-color: #F8FAFC;"
                    + "-fx-background-radius: 10;"
                    + "-fx-border-color: " + BORDER + ";"
                    + "-fx-border-radius: 10;"
                    + "-fx-padding: 10 12 10 12;"
                    + "-fx-text-fill: #334155;"
                    + "-fx-font-size: 12;"
                    + "-fx-font-family: \"Consolas\", \"Courier New\", monospace;";

    private static final String EXAMPLE_BOX_STYLE =
            "-fx-background-color: #F8FAFC;"
                    + "-fx-background-radius: 10;"
                    + "-fx-border-color: " + BORDER + ";"
                    + "-fx-border-radius: 10;"
                    + "-fx-padding: 8 10 8 10;"
                    + "-fx-text-fill: " + TEXT_MUTED + ";"
                    + "-fx-font-size: 11;"
                    + "-fx-font-family: \"Consolas\", \"Courier New\", monospace;";

    private final Image helpIcon = new Image(
            getClass().getResourceAsStream("/images/help.png"));

    private final Stage helpStage;

    @FXML
    private VBox helpContent;

    @FXML
    private VBox helpSectionsContainer;

    /**
     * Constructs a {@code HelpWindow}.
     */
    public HelpWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelpWindow.class.getResource(FXML));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            buildHelpContent();

            helpStage = new Stage();
            helpStage.getIcons().add(helpIcon);
            helpStage.setTitle(TITLE);
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            scene.getStylesheets().add(
                    getClass().getResource("/view/styles.css")
                            .toExternalForm());
            helpStage.setScene(scene);
            helpStage.initModality(Modality.NONE);
            helpStage.setMinWidth(620);
            helpStage.setMinHeight(540);
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

        if (helpStage.isIconified()) {
            helpStage.setIconified(false);
        }

        helpStage.toFront();
        helpStage.requestFocus();
    }

    private void buildHelpContent() {
        helpContent.setFillWidth(true);
        helpSectionsContainer.setFillWidth(true);

        helpSectionsContainer.getChildren().setAll(
                createGuideCard(),
                createManagingExpensesSection(),
                createSettlingDebtsSection(),
                createViewingSection(),
                createOtherSection());
    }

    private VBox createGuideCard() {
        VBox card = new VBox(10);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(CARD_STYLE);
        card.setPadding(new Insets(14, 16, 14, 16));

        HBox headerRow = new HBox(8);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("How to read commands");
        titleLabel.setStyle("-fx-font-size: 15;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: " + TEXT_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badgeLabel = createBadge("Guide", SLATE, SLATE_SOFT);

        headerRow.getChildren().addAll(titleLabel, spacer, badgeLabel);

        Label summaryLabel = new Label(
                "Replace the placeholder words with your own values before sending the command.");
        summaryLabel.setWrapText(true);
        summaryLabel.setStyle("-fx-font-size: 11;"
                + "-fx-text-fill: " + TEXT_MUTED + ";");

        VBox infoRows = new VBox(8);
        infoRows.getChildren().addAll(
                createGuideRow(
                        "UPPERCASE",
                        "Placeholder values such as NAME, GROUP, and AMOUNT."),
                createGuideRow(
                        "[optional]",
                        "Fields inside square brackets can be omitted."),
                createGuideRow(
                        "Repeated prefixes",
                        "Use prefixes like s/PERSON more than once when needed."));

        card.getChildren().addAll(
                headerRow,
                summaryLabel,
                createSeparator(),
                infoRows);

        return card;
    }

    private HBox createGuideRow(String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.TOP_LEFT);

        Label labelNode = new Label(label);
        labelNode.setStyle(
                "-fx-font-size: 11;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: " + TEXT_DARK + ";"
                        + "-fx-min-width: 110;");

        Label valueNode = new Label(value);
        valueNode.setWrapText(true);
        valueNode.setMaxWidth(Double.MAX_VALUE);
        valueNode.setStyle(
                "-fx-font-size: 11;"
                        + "-fx-text-fill: " + TEXT_MUTED + ";");
        HBox.setHgrow(valueNode, Priority.ALWAYS);

        row.getChildren().addAll(labelNode, valueNode);
        return row;
    }

    private VBox createManagingExpensesSection() {
        return createSection(
                "Managing Expenses",
                createCommandCard(
                        "Add an expense",
                        "add",
                        "Create a new expense using either an equal split or a proportional split.",
                        "Equal split:\n"
                                + "add n/NAME a/AMOUNT g/GROUP p/PAYER s/PERSON...\n\n"
                                + "Proportional split:\n"
                                + "add n/NAME a/AMOUNT g/GROUP p/PAYER s/PERSON:PARTS...",
                        "add n/Lunch a/20.0 g/JB p/alice s/alice:3 s/bob:1 t/food",
                        "Use s/alice:3 s/bob:1 for a 3:1 split.",
                        BLUE,
                        BLUE_SOFT),
                createCommandCard(
                        "Delete an expense",
                        "delete",
                        "Remove one expense by using the displayed index in the list.",
                        "delete INDEX",
                        "delete 1",
                        null,
                        BLUE,
                        BLUE_SOFT),
                createCommandCard(
                        "Update an expense",
                        "update",
                        "Edit selected fields on an existing expense without re-adding it.",
                        "update INDEX [n/NAME] [a/AMOUNT] [p/PAYER] [s/PERSON...] [t/TAG] [g/GROUP]",
                        "update 2 a/50.0 p/bob",
                        "Only include the fields that you want to change.",
                        BLUE,
                        BLUE_SOFT),
                createCommandCard(
                        "Clear all expenses",
                        "clear",
                        "Remove every expense currently recorded in the list.",
                        "clear",
                        null,
                        "Use this when you want to start fresh with an empty list.",
                        BLUE,
                        BLUE_SOFT));
    }

    private VBox createSettlingDebtsSection() {
        return createSection(
                "Settling Balances",
                createCommandCard(
                        "Settle a balance",
                        "settle",
                        "Record a payment from one member to another and reduce the outstanding balance.",
                        "settle g/GROUP p/PAYER r/RECEIVER a/AMOUNT",
                        "settle g/JB p/alice r/bob a/10.0",
                        null,
                        BLUE,
                        BLUE_SOFT));
    }

    private VBox createViewingSection() {
        return createSection(
                "Viewing and Searching",
                createCommandCard(
                        "Filter expenses",
                        "filter",
                        "Show only expenses that match a group, expense name, payer, participant, or tag.",
                        "filter g/GROUP   by group\n"
                                + "filter n/NAME    by expense name\n"
                                + "filter p/PAYER   by payer\n"
                                + "filter s/PERSON  by participant\n"
                                + "filter t/TAG     by tag",
                        "filter g/JB",
                        "Run list afterwards to show all expenses again.",
                        BLUE,
                        BLUE_SOFT),
                createCommandCard(
                        "List all expenses",
                        "list",
                        "Restore the full expense list after filtering.",
                        "list",
                        null,
                        null,
                        BLUE,
                        BLUE_SOFT));
    }

    private VBox createOtherSection() {
        return createSection(
                "Other Commands",
                createCommandCard(
                        "Display help window",
                        "help",
                        "Open this help window at any time.",
                        "help",
                        null,
                        null,
                        BLUE,
                        BLUE_SOFT),
                createCommandCard(
                        "Exit FairShare",
                        "exit",
                        "Terminates the program.",
                        "exit",
                        null,
                        null,
                        BLUE,
                        BLUE_SOFT));
    }

    private VBox createSection(String title, VBox... commandCards) {
        VBox section = new VBox(12);
        section.setMaxWidth(Double.MAX_VALUE);
        section.setFillWidth(true);

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 13;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: " + HEADING + ";");

        VBox cardsContainer = new VBox(12);
        cardsContainer.setFillWidth(true);
        cardsContainer.getChildren().addAll(commandCards);

        section.getChildren().addAll(titleLabel, cardsContainer);
        return section;
    }

    private VBox createCommandCard(String title, String badgeText, String summary, String syntax, String example,
                                   String note, String accentColour, String accentSoftColour) {
        VBox card = new VBox(10);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(CARD_STYLE);
        card.setPadding(new Insets(14, 16, 14, 16));

        HBox headerRow = new HBox(8);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 15;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: " + TEXT_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badgeLabel = createBadge(badgeText, accentColour, accentSoftColour);

        headerRow.getChildren().addAll(titleLabel, spacer, badgeLabel);

        Label summaryLabel = new Label(summary);
        summaryLabel.setWrapText(true);
        summaryLabel.setStyle("-fx-font-size: 11;"
                + "-fx-text-fill: " + TEXT_MUTED + ";");

        VBox contentBox = new VBox(8);
        contentBox.setFillWidth(true);

        contentBox.getChildren().add(createMetaLabel("Syntax"));

        Label syntaxLabel = new Label(syntax);
        syntaxLabel.setWrapText(true);
        syntaxLabel.setMaxWidth(Double.MAX_VALUE);
        syntaxLabel.setStyle(CODE_BOX_STYLE);
        contentBox.getChildren().add(syntaxLabel);

        if (example != null && !example.isBlank()) {
            HBox exampleHeader = new HBox(8);
            exampleHeader.setAlignment(Pos.CENTER_LEFT);

            Label exampleMetaLabel = createMetaLabel("Example");

            Region exampleSpacer = new Region();
            HBox.setHgrow(exampleSpacer, Priority.ALWAYS);

            Button copyButton = createCopyButton(example);

            exampleHeader.getChildren().addAll(
                    exampleMetaLabel, exampleSpacer, copyButton);

            Label exampleLabel = new Label(example);
            exampleLabel.setWrapText(true);
            exampleLabel.setMaxWidth(Double.MAX_VALUE);
            exampleLabel.setStyle(EXAMPLE_BOX_STYLE);

            contentBox.getChildren().addAll(exampleHeader, exampleLabel);
        }

        if (note != null && !note.isBlank()) {
            Label noteLabel = new Label(note);
            noteLabel.setWrapText(true);
            noteLabel.setMaxWidth(Double.MAX_VALUE);
            noteLabel.setStyle("-fx-font-size: 11;"
                    + "-fx-text-fill: " + INFO + ";"
                    + "-fx-background-color: " + INFO_SOFT + ";"
                    + "-fx-background-radius: 10;"
                    + "-fx-padding: 8 10 8 10;");
            contentBox.getChildren().add(noteLabel);
        }

        card.getChildren().addAll(
                headerRow,
                summaryLabel,
                createSeparator(),
                contentBox);

        return card;
    }

    private Label createBadge(String text, String accentColour, String accentSoftColour) {
        Label badgeLabel = new Label(text);
        badgeLabel.setStyle("-fx-font-size: 10;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: " + accentColour + ";"
                + "-fx-background-color: " + accentSoftColour + ";"
                + "-fx-background-radius: 999;"
                + "-fx-padding: 4 10 4 10;");
        return badgeLabel;
    }

    private Label createMetaLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 10;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #94A3B8;");
        return label;
    }

    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + BORDER + ";");
        return separator;
    }

    private Button createCopyButton(String textToCopy) {
        Button copyButton = new Button("copy");
        copyButton.setFocusTraversable(false);
        copyButton.setStyle(
                "-fx-background-color: #FFFFFF;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 6;"
                        + "-fx-background-radius: 6;"
                        + "-fx-text-fill: " + TEXT_MUTED + ";"
                        + "-fx-font-size: 10;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 2 8 2 8;");
        copyButton.setOnAction(event -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(textToCopy);
            Clipboard.getSystemClipboard().setContent(content);
        });
        return copyButton;
    }
}
