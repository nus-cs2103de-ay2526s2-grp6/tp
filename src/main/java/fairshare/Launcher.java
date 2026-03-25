package fairshare;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues.
 */
public class Launcher {

    /**
     * Entry point of the application.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Application.launch(FairShare.class, args);
    }
}
