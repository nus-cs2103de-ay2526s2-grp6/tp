package fairshare.ui;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;

/**
 * Represents a generic UI component loaded from an FXML file.
 * All UI panel and window classes should extend this class,
 * providing the FXML filename and the root node type.
 *
 * @param <T> the type of the root node of this UI component.
 */
public abstract class UiPart<T> {

    private static final String FXML_FILE_FOLDER = "/view/";

    private final FXMLLoader fxmlLoader = new FXMLLoader();

    /**
     * Constructs a {@code UiPart} and loads the FXML file with the given name.
     *
     * @param fxmlFileName the name of the FXML file to load; cannot be null.
     */
    public UiPart(String fxmlFileName) {
        loadFxmlFile(getFxmlFileUrl(fxmlFileName));
    }

    /**
     * Returns the root node of this UI component.
     *
     * @return the root {@code T} node loaded from FXML.
     */
    public T getRoot() {
        return fxmlLoader.getRoot();
    }

    /**
     * Loads the FXML file from the given URL and sets this object
     * as both the controller and root.
     *
     * @param location the URL of the FXML file; cannot be null.
     */
    private void loadFxmlFile(URL location) {
        fxmlLoader.setLocation(location);
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML file: "
                    + location.toExternalForm(), e);
        }
    }

    /**
     * Returns the URL for the FXML file with the given name
     * from the resources view folder.
     *
     * @param fxmlFileName the FXML file name; cannot be null.
     * @return the URL pointing to the FXML resource.
     * @throws RuntimeException if the FXML file cannot be found.
     */
    private URL getFxmlFileUrl(String fxmlFileName) {
        URL fileUrl = getClass().getResource(
                FXML_FILE_FOLDER + fxmlFileName);

        if (fileUrl == null) {
            throw new RuntimeException(
                    "FXML file not found: " + fxmlFileName);
        }

        return fileUrl;
    }
}
